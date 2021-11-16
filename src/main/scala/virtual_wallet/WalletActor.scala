package main.scala.virtual_wallet

import java.util.UUID

import akka.actor.{Actor, ActorLogging, ActorRef, Props, Stash}
import akka.cluster.sharding.ShardRegion.{ExtractEntityId, ExtractShardId}
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import com.fasterxml.jackson.annotation.JsonTypeInfo
import main.scala.virtual_wallet.Entity._

import scala.language.postfixOps
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}

object WalletActor {

  @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, property = "type")
  trait SerializableMessage

  trait Command extends SerializableMessage { val userCuit: String }
  trait Query extends SerializableMessage { val userCuit: String }
  trait Event extends SerializableMessage
  trait Response extends SerializableMessage

  case class CreateWallet(userCuit: String) extends Command
  case class Recharge(userCuit: String, value: BigDecimal) extends Command
  case class Consume(userCuit: String, value: BigDecimal) extends Command
  case class Transfer(userCuit: String, value: BigDecimal, wallet: Option[ActorRef]) extends Command
  case class UpdateUserData(userCuit: String, user: User) extends Command

  case class WalletCreated(userCuit: String) extends Event
  case class Recharged(value: BigDecimal) extends Event
  case class Consumed(value: BigDecimal) extends Event
  case class UserDataUpdated(user: User) extends Event
  case class SuccessfulTransfer(value: BigDecimal, user: User, destination: Boolean) extends Event
  case class FailedTransfer(value: BigDecimal, user: User, ex: Exception) extends Event

  case class WalletBalance(balance: BigDecimal) extends Response
  case class WalletUser(user: User) extends Response

  case class GetBalance(userCuit: String) extends Query
  case class GetUserInfo(userCuit: String) extends Query

  case class WalletNotFoundException(walletId: WalletId) extends IllegalStateException(s"Wallet Not Found: $walletId")
  case class WalletNotInitializedException() extends IllegalStateException(s"Wallet with that cuit not initialized")
  case class WalletNotEnoughBalanceException(walletId: WalletId) extends IllegalStateException(s"Wallet Not Enough Balance: $walletId")
  case class FailedTransferException(user: String, ex: Exception) extends IllegalStateException(s"The transfer to $user failed: ${ex.getMessage}")

  def props(): Props = Props(new WalletActor())

  val entityIdExtractor: ExtractEntityId = {
    case c: Command => (c.userCuit, c)
    case q: Query => (q.userCuit, q)
  }

  val shardIdExtractor: ExtractShardId = {
    case c: Command => Math.abs(c.userCuit.hashCode % 30).toString
    case q: Query => Math.abs(q.userCuit.hashCode % 30).toString
  }
}

class WalletActor extends Actor with ActorLogging with Stash {
  import WalletActor._
  import context.dispatcher
  implicit val timeout: Timeout = Timeout(5 seconds)

  var walletId: WalletId = WalletId()
  var wallet: Wallet = Wallet()

  override def receive: Receive = {
    case c: Command =>
      context.become(handleCommand(sender()))
      self ! c
    case q: Query =>
      context.become(handleQuery(sender()))
      self ! q
  }

  def handleCommand(sender: ActorRef): Receive = {
    case CreateWallet(cuit) =>
      log.info(s"--- CreateWallet($cuit) ---")
      walletId = WalletId()
      context.become(handleEvent(sender))
      self ! WalletCreated(cuit)

    case Recharge(_, value) =>
      log.info(s"[${walletId.value}] Recharge($value)")
      context.become(handleEvent(sender))
      self ! Recharged(value)

    case Consume(_, value) =>
      log.info(s"[${walletId.value}] Consume($value)")
      context.become(handleEvent(sender))
      self ! Consumed(value)

    case UpdateUserData(_, user) =>
      log.info(s"[${walletId.value}] UpdateUserData($user)")
      context.become(handleEvent(sender))
      self ! UserDataUpdated(user)

    case cmd @ Transfer(_, value, _) =>
      log.info(s"[${walletId.value}] Transfer($value)")
      context.become(handleTransfer(sender))
      self ! cmd
  }

  def handleEvent(sender: ActorRef): Receive = {
    case WalletCreated(cuit) =>
      log.info(s"[${walletId.value}] WalletCreated($cuit)")
      wallet = Wallet(walletId, User(None, cuit, None, None))
      sender ! wallet
      context.become(receive)

    case Recharged(value) =>
      log.info(s"[${walletId.value}] Recharged($value)")
      if (wallet.isEmpty())
        Future.failed(WalletNotInitializedException()).pipeTo(sender)
      else {
        wallet = wallet += value
        sender ! wallet
      }
      context.become(receive)

    case Consumed(value) =>
      log.info(s"[${walletId.value}] Consumed($value)")
      if (wallet.isEmpty())
        Future.failed(WalletNotInitializedException()).pipeTo(sender)
      else {
        if(wallet.balance - value < 0)
          Future.failed(WalletNotEnoughBalanceException(walletId)).pipeTo(sender)
        else {
          wallet = wallet -= value
          sender ! wallet
        }
      }
      context.become(receive)

    case UserDataUpdated(user) =>
      log.info(s"[${walletId.value}] UserDataUpdated($user)")
      if (wallet.isEmpty())
        Future.failed(WalletNotInitializedException()).pipeTo(sender)
      else {
        wallet = wallet updateUser user
        sender ! wallet
      }
      context.become(receive)

    case SuccessfulTransfer(value, user, destination) =>
      log.info(s"[${walletId.value}] SuccessfulTransfer($value, $user, $destination)")
      if(!destination)
        sender ! SuccessfulTransfer(value, wallet.user, destination)
      context.become(receive)
      unstashAll()

    case FailedTransfer(value, user, ex) =>
      log.info(s"[${walletId.value}] FailedTransfer($value, $user, $ex)")
      Future.failed(ex).pipeTo(sender)
      context.become(receive)
      unstashAll()

    case _ => stash()
  }

  def handleQuery(sender: ActorRef): Receive = {
    case GetBalance(_) =>
      log.info(s"[${walletId.value}] GetBalance()")
      if (wallet.isEmpty())
        Future.failed(WalletNotInitializedException()).pipeTo(sender)
      else
        sender ! WalletBalance(wallet.balance)
      context.become(receive)
    case GetUserInfo(_) =>
      log.info(s"[${walletId.value}] GetUserInfo()")
      if (wallet.isEmpty())
        Future.failed(WalletNotInitializedException()).pipeTo(sender)
      else
        sender ! WalletUser(wallet.user)
      context.become(receive)
  }

  def handleTransfer(sender: ActorRef): Receive = {
    case Transfer(userCuit, value, actorRef) =>
      log.info(s" Transferring ... ")
      actorRef match {
        case Some(actor) => { // Billetera inicial
          context.become(handleEvent(sender)) // Queda escuchando eventos
          if((wallet.balance - value) < 0) // si no tiene saldo para transferir
            self ! FailedTransfer(value, wallet.user, WalletNotEnoughBalanceException(walletId)) // falla la transferencia y lo notifica a traves de un evento
          else {
            (actor ? Transfer(userCuit, value, None)) onComplete { // envia la transferencia a la otra billetera
              case Success(event) => { // si tiene exito
                wallet = wallet -= value // descuenta la plata de la billetera
                self ! event // se notifica a si mismo a traves de un evento que  fue un exito
              }
              case Failure(ex: Exception) => // en el caso de que falle se lo notifica a si mismo a traves de un evento
                self ! FailedTransfer(value, wallet.user, ex)
            }
          }
        }
        case None => { // Billetera destino
          if(wallet.isEmpty()) { // si no esta inicializada
            Future.failed(FailedTransferException(wallet.user.cuit, WalletNotInitializedException())).pipeTo(sender) // devuelve un error
            context.become(receive)
            unstashAll()
          } else { // si esta inicializada
            context.become(handleEvent(sender))
            wallet = wallet += value // suma su valor
            sender ! SuccessfulTransfer(value, wallet.user, false) // notifica a la cuenta inicial traves de un mensaje el exito
            self ! SuccessfulTransfer(value, wallet.user, true) // se notificica del exito
          }
        }
      }
    case _ =>
      log.info(s" Transferring ... ")
      stash()
  }

}