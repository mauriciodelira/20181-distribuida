package models

case class Filme(
                id: Long,
                titulo: String,
                diretor: String,
                estudio: String,
                genero: String,
                ano: Int,
                )
object Filme {
  import play.api.libs.json.{OFormat, Json}
  implicit val format: OFormat[Filme] = Json.format[Filme]
}
