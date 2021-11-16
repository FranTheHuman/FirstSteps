import java.util.UUID

import akka.actor.ActorSystem
import akka.actor.Status.Failure
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import main.scala.virtual_wallet.Entity.{User, Wallet, WalletId}
import main.scala.virtual_wallet.WalletActor
import main.scala.virtual_wallet.WalletActor.{Consume, CreateWallet, Event, FailedTransferException, Recharge, SuccessfulTransfer, Transfer, UpdateUserData, WalletNotEnoughBalanceException, WalletNotInitializedException}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import scala.language.postfixOps

class VirtualWalletSpec
    extends TestKit(ActorSystem("VirtualWalletSpec")) // implicit actor system
    with ImplicitSender // testActor for implicit sender
    with AnyWordSpecLike //
    with Matchers // ScalaTest provides a domain specific language (DSL) for expressing assertions in tests using the word should
    with BeforeAndAfterAll// Trait BeforeAndAfterAll defines two overloaded variants each of beforeAll and afterAll
{

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "An Virtual Wallet Actor" should {

    var walletIdTest: WalletId = WalletId()
    val cuit: String = "25-41411125-5"
    val userTest: User = User(None, cuit, None, None)
    val virtualWallet = system.actorOf(WalletActor.props())

    "should fail to recharge balance" in {
      val amount: BigDecimal = BigDecimal(1000000)
      virtualWallet ! Recharge(cuit, amount)
      expectMsg(Failure(WalletNotInitializedException()))
    }

    "should be created" in {
      virtualWallet ! CreateWallet(cuit)
      expectMsgPF() {
        case Wallet(walletId, User(None, "25-41411125-5", None, None), _) => walletIdTest = walletId
      }
    }

    "should recharge balance" in {
      val amount: BigDecimal = BigDecimal(1000000)
      virtualWallet ! Recharge(cuit, amount)
      expectMsg(Wallet(walletIdTest, userTest, amount))
    }


    "should fail to consume balance" in {
      val amount: BigDecimal = BigDecimal(2000000)
      virtualWallet ! Consume(cuit, amount)
      expectMsg(Failure(WalletNotEnoughBalanceException(walletIdTest)))
    }

    "should consume balance" in {
      val amount: BigDecimal = BigDecimal(5000)
      virtualWallet ! Consume(cuit, amount)
      expectMsg(Wallet(walletIdTest, userTest, (BigDecimal(1000000) - amount)))
    }


    "should update user data" in {
      val userData: User = User(Some("Francisco Perrotta"), "25-41411125-5", Some("3517720604"), Some("perrottafrancisco@gmail.com"))
      virtualWallet ! UpdateUserData(cuit, userData)
      expectMsg(Wallet(walletIdTest, userData, BigDecimal(995000)))
    }

  }

  "An Virtual Wallet ActorA And Virtual Wallet ActorB" should {

    var walletId: WalletId = WalletId()
    val cuitA: String = "25-41411125-5"
    val cuitB: String = "20-41224428-0"
    val userTestA: User = User(None, cuitA, None, None)
    val userTestB: User = User(None, cuitB, None, None)
    val virtualWalletB_TEST = TestActorRef(new WalletActor)
    val virtualWallet = system.actorOf(WalletActor.props())


    "should recharge balance" in {
      virtualWallet ! CreateWallet(cuitA)
      expectMsgPF() {
        case Wallet(id, _, _) => walletId = id
      }
      val amount: BigDecimal = BigDecimal(1000000)
      virtualWallet ! Recharge(cuitA, amount)
      expectMsgType[Wallet]
    }

    "should transfer balance" in {
      virtualWalletB_TEST ! CreateWallet(cuitB)
      expectMsgType[Wallet]
      virtualWallet ! Transfer(cuitA,BigDecimal(500000), Some(virtualWalletB_TEST))
      expectMsgType[SuccessfulTransfer]
    }

    "should fail to transfer balance by WalletNotEnoughBalanceException" in {
      virtualWallet ! Transfer(cuitA,BigDecimal(1000000), Some(virtualWalletB_TEST))
      expectMsg(Failure(WalletNotEnoughBalanceException(walletId)))
    }
  }

}
