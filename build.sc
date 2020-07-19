import $file.dbgenerator
import mill._
import mill.scalalib._
import os.Path
import slick.jdbc.JdbcProfile

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}

trait BaseModule extends SbtModule {
  override def scalaVersion = "2.12.10"

  def scalaPath: Path = millSourcePath / 'src / 'main / 'scala
}

object app extends BaseModule {
  override def moduleDeps = zoodb :: Nil

  override def ivyDeps = Agg(
    ivy"com.typesafe.slick::slick:3.3.2",
    ivy"org.slf4j:slf4j-simple:1.7.30",
  )

}

object zoodb extends BaseModule {
  override def moduleDeps = profile :: tables :: Nil

  object tables extends BaseModule {
    override def moduleDeps = profile :: Nil

    override def generatedSources = T {
      val profile: String = "slick.jdbc.PostgresProfile"
      val jdbcDriver: String = "org.postgresql.Driver"
      val url: String = "jdbc:postgresql://localhost/zoo"
      val pkg: String = "zoodb.tables"

      val profileInstance: JdbcProfile = Class.forName(profile + "$").getField("MODULE$").get(null).asInstanceOf[JdbcProfile]
      val dbFactory = profileInstance.api.Database
      val db = dbFactory.forURL(url, driver = jdbcDriver, user = null, password = null)
      val model = Await.result(db.run(profileInstance.createModel(None, ignoreInvalidDefaults = true)(ExecutionContext.global).withPinnedSession), Duration.Inf)
      val generator = new dbgenerator.CustomGenerator(model)

      // Generates output files to a temporary directory
      val tempDirPath = os.temp.dir() / 'generated

      generator.generateTableCode("zoodb.profile.ZooDbPostgresProfile", tempDirPath, pkg)

      PathRef(tempDirPath) :: Nil
    }
  }

  object profile extends BaseModule {
    override def ivyDeps = Agg(
      ivy"com.typesafe.slick::slick:3.3.2",
      ivy"com.github.tminglei::slick-pg:0.17.2",
      ivy"com.github.tminglei::slick-pg_joda-time:0.17.2",
      ivy"org.postgresql:postgresql:42.2.5",
    )
  }

}
