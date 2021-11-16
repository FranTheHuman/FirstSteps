package virtual_wallet

import akka.actor.ActorRef
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import akka.pattern.ask

import scala.language.postfixOps
import akka.util.Timeout
import main.scala.virtual_wallet.Entity.{Wallet, WalletId}
import main.scala.virtual_wallet.WalletActor.{Consume, CreateWallet, GetBalance, GetUserInfo, Recharge, Recharged, WalletBalance, WalletNotEnoughBalanceException, WalletNotFoundException, WalletNotInitializedException, WalletUser}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

class WalletRoutes(walletActor: ActorRef)(implicit ec: ExecutionContext) extends WalletJsonFormats {

  implicit val timeout: Timeout = Timeout(5 seconds)  // -> for ask patter

  implicit def exceptionhandler: ExceptionHandler = ExceptionHandler {
    case ex: WalletNotFoundException =>
      complete(HttpResponse(StatusCodes.NotFound, entity = ex.getMessage))
    case ex: WalletNotInitializedException =>
      complete(HttpResponse(StatusCodes.BadRequest, entity = ex.getMessage))
    case ex: WalletNotEnoughBalanceException =>
      complete(HttpResponse(StatusCodes.Conflict, entity = ex.getMessage))
    case ex =>
      complete(HttpResponse(StatusCodes.InternalServerError, entity = ex.getMessage))
  }

  lazy val routes: Route = Route.seal(
      pathPrefix("virtualWallet") {
        pathPrefix(Segment) { cuit =>
          post {
            path("create") {
              complete {
                (walletActor ? CreateWallet(cuit))
                  .mapTo[Wallet]
              }
            } ~
            path("recharge") {
              entity(as[Recharge]) { recharge =>
                complete {
                  (walletActor ? Recharge(cuit, recharge.value))
                    .mapTo[Wallet]
                }
              }
            } ~
            path("consume") {
              entity(as[Consume]) { consume =>
                complete {
                  (walletActor ? Consume(cuit, consume.value))
                    .mapTo[Wallet]
                }
              }
            }
          } ~
          get {
            pathPrefix("getBalance") {
              complete {
                (walletActor ? GetBalance(cuit))
                  .mapTo[WalletBalance]
              }
            } ~
            pathPrefix("getUser") {
              complete {
                (walletActor ? GetUserInfo(cuit))
                  .mapTo[WalletUser]
              }
            }
          }
        }
      }
  )

}
