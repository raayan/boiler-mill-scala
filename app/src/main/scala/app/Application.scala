package app

import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory
import zoodb.mammalia.Order
import zoodb.profile.ZooDbFactory
import zoodb.profile.ZooDbPostgresProfile.api._
import zoodb.tables.Tables

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Awaitable}

object Application {
  def main(args: Array[String]): Unit = {
    val logger = LoggerFactory.getLogger(getClass)

    val config = ConfigFactory.load()

    logger.info("Connecting to DB")
    val db = ZooDbFactory.connect(config, "zoo.db")

    val queryResults = await {
      db.run {
        Tables.Animals.result
      }
    }

    logger.info(
      s"""
         |Query Results:
         |${queryResults.mkString("\n")}
         |""".stripMargin)

    val insertResult = await {
      db.run {
        Tables.Animals.map(animal => (animal.name, animal.mammaliaOrder)).returning(Tables.Animals) += ("Polo", Order.Carnivora)
      }
    }

    logger.info(
      s"""
         |Insert Result:
         |$insertResult
         |""".stripMargin)

  }

  private def await[T](f: Awaitable[T]) = Await.result(f, Duration("1 second"))
}
