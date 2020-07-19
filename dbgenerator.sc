import $ivy.`com.typesafe.slick::slick-codegen:3.3.2`
import $ivy.`org.postgresql:postgresql:42.2.5`
import mill.api.PathRef
import os.Path
import slick.codegen.SourceCodeGenerator
import slick.model.Model
import slick.sql.SqlProfile.ColumnOption.SqlType

class CustomGenerator(model: Model) extends SourceCodeGenerator(model) {
  def generateTableCode(profile: String, sourceFolder: Path, pkg: String, container: String = "Tables"): List[PathRef] = {
    val containerFileName = container + ".scala"
    val containerPath = sourceFolder / containerFileName

    writeStringToFile(packageContainerCode(profile, pkg, container), sourceFolder.toString(), pkg, containerPath.last)
    val tablesPaths = codePerTable.map {
      case (tableName, tableCode) =>
        val tablePath = sourceFolder / s"${handleQuotedNamed(tableName)}.scala"
        writeStringToFile(packageTableCode(tableName, tableCode, pkg, container), sourceFolder.toString(), pkg, tablePath.last)
        PathRef(tablePath)
    }.toList

    PathRef(containerPath) :: tablesPaths
  }

  override def packageContainerCode(profile: String, pkg: String, container: String) = {
    val mixinCode = codePerTable.keys.map(tableName => s"${this.handleQuotedNamed(tableName)}").mkString("extends ", " with ", "")
    s"""
       |package $pkg
       |// AUTO-GENERATED Slick data model
       |/** Stand-alone Slick data model for immediate use */
       |object $container extends {
       |  val profile = $profile
       |} with $container
       |
       |trait $container${parentType.map(t => s" extends $t").getOrElse("")} $mixinCode {
       |  val profile: $profile
       |  import profile.api._
       |  ${indent(codeForContainer)}
       |
       |}
       |""".stripMargin.trim()
  }

  private def handleQuotedNamed(tableName: String) = {
    if (tableName.endsWith("`")) s"${tableName.init}Table`" else s"${tableName}Table"
  }

  override def code: String = super.code

  override def tableName = super.tableName

  override def entityName = super.entityName

  override def Table = new Table(_) {
    override def Column = new Column(_) {
      override def rawType = {
        model.options.toList match {
          case SqlType(typeName) :: _ =>
            typeName match {
              case "mammalia_order" => "zoodb.mammalia.Order"
              case _ => super.rawType
            }
          case _ => super.rawType
        }
      }
    }
  }
}