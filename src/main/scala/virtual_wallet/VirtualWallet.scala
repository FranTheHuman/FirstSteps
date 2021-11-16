package main.scala.virtual_wallet

import akka.actor.{ActorSystem}
import akka.cluster.sharding.{ClusterSharding, ClusterShardingSettings}
import akka.http.scaladsl.Http
import akka.management.scaladsl.AkkaManagement
import virtual_wallet.WalletRoutes

import scala.concurrent.ExecutionContext
import scala.language.postfixOps

object VirtualWallet extends App {

  implicit val system = ActorSystem("VirtualWallet")
  implicit val ec: ExecutionContext = system.dispatcher
  // implicit val timeout: Timeout = Timeout(5 seconds)

  // val virtualWallet = actorSystem.actorOf(WalletActor.props(), "wallet")
  // val virtualWallet2 = actorSystem.actorOf(WalletActor.props(), "wallet2")

  AkkaManagement(system).start()

  // Cluster
  val virtualWallet = ClusterSharding(system).start(
    "virtualWallet",
    WalletActor.props(),
    ClusterShardingSettings(system),
    WalletActor.entityIdExtractor,
    WalletActor.shardIdExtractor
  )

  Http().newServerAt("localhost", 7000).bind(new WalletRoutes(virtualWallet).routes)
  println(s"Server online at http://localhost:7000/\nPress RETURN to stop...")
}
