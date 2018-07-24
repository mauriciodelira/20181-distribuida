package models.forms

import play.api.data.Form
import play.api.data.Forms._

case class CreateFilmeForm(id: Option[Long],
                           titulo: String,
                           diretor: String,
                           estudio: String,
                           genero: String,
                           ano: Int,
                         )

object CreateFilmeForm {
  val form: Form[CreateFilmeForm] = Form {
    mapping(
      "id" -> optional(longNumber),
      "titulo" -> nonEmptyText,
      "diretor" -> nonEmptyText,
      "estudio" -> nonEmptyText,
      "genero" -> nonEmptyText,
      "ano" -> number
    )(CreateFilmeForm.apply)(CreateFilmeForm.unapply)
  }

  val onlyIdForm: Form[Long] = Form {
   mapping(
     "id" -> longNumber
   )(id => id)(id => Option(id))
  }
}
