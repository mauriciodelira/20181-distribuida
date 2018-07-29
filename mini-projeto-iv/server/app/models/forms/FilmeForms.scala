package models.forms

import play.api.data.Form
import play.api.data.Forms._

case class FilmeForms(id: Option[Long],
                      titulo: String,
                      diretor: String,
                      estudio: String,
                      genero: String,
                      ano: Int,
                         )

object FilmeForms {
  def unapplyCreate: FilmeForms => Option[(String, String, String, String, Int)] = x =>
    Option((x.titulo, x.diretor, x.estudio, x.genero, x.ano))

  val createMovieForm: Form[FilmeForms] = Form {
    mapping(
      "titulo" -> nonEmptyText,
      "diretor" -> nonEmptyText,
      "estudio" -> nonEmptyText,
      "genero" -> nonEmptyText,
      "ano" -> number
    )(FilmeForms.apply(None, _, _, _, _, _))(FilmeForms.unapplyCreate)
  }

  val onlyIdForm: Form[Long] = Form {
   mapping(
     "id" -> longNumber
   )(id => id)(id => Option(id))
  }
  val idAndTypeForm: Form[(Long, String)] = Form {
   mapping(
     "id" -> longNumber,
     "type" -> nonEmptyText(3, 4)
   )((a, b) => (a, b))(tuple => Option((tuple._1, tuple._2)))
  }
}
