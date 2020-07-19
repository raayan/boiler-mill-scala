package zoodb

object Helpers {

  trait DbEnum extends Product {
    val sqlValue: String = productPrefix.toLowerCase
  }

  abstract class DbEnumHelper[T <: DbEnum] {
    val values: Set[T]
    val fromSqlValue: String => T = sqlValue => values.find(_.sqlValue == sqlValue).get
    implicit val self: DbEnumHelper[T] = this
  }

}
