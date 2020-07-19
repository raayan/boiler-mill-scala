package zoodb.profile

import com.github.tminglei.slickpg.{ExPostgresProfile, PgEnumSupport}
import com.typesafe.config.Config
import slick.basic.Capability
import slick.jdbc.JdbcType
import zoodb.Helpers.{DbEnum, DbEnumHelper}
import zoodb.mammalia
import zoodb.profile.ZooDbPostgresProfile.ZooDb

import scala.reflect.ClassTag

object ZooDbFactory {
  def connect(config: Config, path: String): ZooDb = {
    ZooDbPostgresProfile.backend.createDatabase(config, path)
  }
}

trait ZooDbPostgresProfile extends ExPostgresProfile with PgEnumSupport {
  override val api = ZooDbApi

  def createPgEnum[T <: DbEnum : ClassTag : DbEnumHelper](enumName: String): JdbcType[T] = {
    createEnumJdbcType[T](enumName, _.sqlValue, implicitly[DbEnumHelper[T]].fromSqlValue, quoteName = false)
  }

  override protected def computeCapabilities: Set[Capability] = super.computeCapabilities

  object ZooDbApi extends API {
    implicit val mammaliaOrderEnumType: JdbcType[mammalia.Order] = createPgEnum[mammalia.Order]("mammalia_order")
  }

}

object ZooDbPostgresProfile extends ZooDbPostgresProfile {
  type ZooDb = backend.Database
}
