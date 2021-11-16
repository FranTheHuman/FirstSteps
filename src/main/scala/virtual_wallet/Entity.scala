package main.scala.virtual_wallet

import java.util.UUID

import akka.actor.ActorRef
import akka.pattern.pipe
import main.scala.virtual_wallet.WalletActor.{SerializableMessage, WalletNotEnoughBalanceException, WalletNotInitializedException}

import scala.concurrent.Future

object Entity {

  object WalletId {
    def apply(): WalletId = WalletId(UUID.randomUUID())
  }

  case class WalletId(value: UUID)
  case class User(name: Option[String], cuit: String, phone: Option[String], mail: Option[String]) {
    def update(name: Option[String], phone: Option[String], mail: Option[String]): User = {
      copy(
        name = name,
        phone = phone,
        mail = mail,
      )
    }
  }



  case class Wallet(id: WalletId = WalletId(), user: User = User(None, "", None, None), balance: BigDecimal = 0) extends SerializableMessage {
    def +=(amount: BigDecimal): Wallet = {
      copy(
        balance = (balance + amount).setScale(2, BigDecimal.RoundingMode.HALF_UP)
      )
    }
    def -=(amount: BigDecimal): Wallet = {
      copy(
        balance = (balance - amount).setScale(2, BigDecimal.RoundingMode.HALF_UP)
      )
    }
    def updateUser(newUser: User): Wallet = {
      copy(
        user = user.update(newUser.name, newUser.phone, newUser.mail)
      )
    }
    def isEmpty(): Boolean = user.cuit.size == 0
  }

}

