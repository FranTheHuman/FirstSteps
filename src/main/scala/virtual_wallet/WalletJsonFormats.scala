package virtual_wallet

import java.util.UUID

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import main.scala.virtual_wallet.Entity.{User, Wallet, WalletId}
import main.scala.virtual_wallet.WalletActor.{Consume, Consumed, CreateWallet, GetBalance, GetUserInfo, Recharge, Recharged, UpdateUserData, UserDataUpdated, WalletBalance, WalletCreated, WalletUser}
import spray.json.{DefaultJsonProtocol, DeserializationException, JsField, JsNumber, JsObject, JsString, JsValue, JsonFormat}

class WalletJsonFormats extends SprayJsonSupport with DefaultJsonProtocol {

    // Entities
    implicit val walletIdFormat = new JsonFormat[WalletId] { // Object Json Format
        override def write(obj: WalletId): JsValue = JsString(obj.value.toString)
        override def read(json: JsValue): WalletId = {
            json match {
                case JsString(uuid) => WalletId(UUID.fromString(uuid))
                case _ => throw DeserializationException("Expected UUID String")
            }
        }
    }

    implicit val userFormat = jsonFormat4(User)
    /*
    implicit val walletFormat = new JsonFormat[Wallet] {
        override def write(obj: Wallet): JsValue = JsObject(
            ("id", walletIdFormat.write(obj.id)),
            ("user", userFormat.write(obj.user)),
            ("balance", JsNumber(obj.balance))
        )

        override def read(json: JsValue): Wallet = json match {
            case JsObject(List(
              ("id", JsString(id)),
              ("user", JsString(user)),
              ("balance", JsString(balance))
            )) =>
                Wallet(walletIdFormat.read(JsString(id)), userFormat.read(JsString(user)), BigDecimal(balance))
            case _ =>
                throw DeserializationException("SearchRequest expected")
        }
    }*/
    implicit val walletFormat = jsonFormat3(Wallet)
    /*
    *
    * new JsonFormat[User] {
        override def write(obj: User): JsValue = JsObject(
            ("name", JsString(obj.name.getOrElse(""))),
            ("cuit", JsString(obj.cuit)),
            ("phone", JsString(obj.phone.getOrElse(""))),
            ("mail", JsString(obj.mail.getOrElse("")))
        )

        override def read(json: JsValue): User = json match {
            case JsObject(List(
                ("name", JsString(name)),
                ("cuit", JsString(cuit)),
                ("phone", JsString(phone)),
                ("mail", JsString(mail))
            )) => {
                val n: Option[String] = if (name.size == 0) None else Some(name)
                val p: Option[String] = if (phone.size == 0) None else Some(phone)
                val m: Option[String] = if (mail.size == 0) None else Some(mail)
                User(n, cuit, p, m)
            }
            case _ =>
                throw DeserializationException("SearchRequest expected")
        }
    }
    *
    * */

    // Commands
    implicit val createWalletFormat = jsonFormat1(CreateWallet)
    implicit val rechargeFormat = jsonFormat2(Recharge)
    implicit val consumeFormat = jsonFormat2(Consume)
    implicit val updateUserDataFormat = jsonFormat2(UpdateUserData)
    // Transfer

    // Events
    implicit val walletCreatedFormat = jsonFormat1(WalletCreated)
    implicit val rechargedFormat = jsonFormat1(Recharged)
    implicit val consumedFormat = jsonFormat1(Consumed)
    implicit val userDataUpdatedFormat = jsonFormat1(UserDataUpdated)

    // Query
    implicit val getBalanceFormat = jsonFormat1(GetBalance)
    implicit val getUserInfoFormat = jsonFormat1(GetUserInfo)

    // Messages
    implicit val  WalletBalanceFormat = jsonFormat1(WalletBalance)
    implicit val  WalletUserFormat = jsonFormat1(WalletUser)
}
