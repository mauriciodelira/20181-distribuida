package v1.filmes

import javax.inject.{Inject, Provider}
import models.forms.CreateFilmeForm
import models.{DuplicateKeyException, Filme, NotFoundException}
import repositories.FilmeRepository
import play.api.{Configuration, Logger}
import play.api.libs.json.{Json, OFormat}
import play.api.mvc.{Action, AnyContent, MessagesAbstractController, MessagesControllerComponents}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try
import scala.xml.Elem

case class FilmeResponse(id: Long, titulo: String, diretor: String, estudio: String, genero: String, ano: Int, link: String)
object FilmeResponse {
  implicit val format: OFormat[FilmeResponse] = Json.format
}

class FilmesController @Inject()(repo: FilmeRepository,
                                 cc: MessagesControllerComponents,
                                 filmesRouter: Provider[FilmesRouter]
                                )(implicit ec: ExecutionContext, configuration: Configuration)
  extends MessagesAbstractController(cc) {
  // GET / > lista todos em JSON por padrão
  def index: Action[AnyContent] = Action.async { implicit request =>
    repo.list().map { filmes =>
      Ok(Json.toJson(filmes.map(createFilmeResponse)))
    }
  }

  // GET /id/xml?id=xxxxx > TODO: ver se deve ter um /xml ou /json no final para retornar ou JSON ou XML.
  def showById(tipo: String = "json"): Action[AnyContent] = Action.async { implicit request =>
    CreateFilmeForm.onlyIdForm.bindFromRequest.fold(
     withErrors => Future(BadRequest(withErrors.errorsAsJson)),
     id => repo.findById(id).map{ maybeFilme =>
      maybeFilme.map{ filme =>
        tipo.toLowerCase match {
          case "json" => Ok(Json.toJson(filme))
          case "xml" => Ok(filmeToXml(createFilmeResponse(filme)))
          case _ => Ok(Json.toJson(filme))
        }
      }.getOrElse(NotFound)
    }
    )
  }

  def showByTitulo(titulo: String): Action[AnyContent] = Action.async { implicit request =>
    repo.findByTitulo(titulo).map{ maybeFilme =>
      maybeFilme.map{ filme =>
       Ok(filmeToHtml(createFilmeResponse(filme))).as(HTML)
      }.getOrElse(NotFound)
    }
  }

  def listByDiretor(diretor: String): Action[AnyContent] = Action.async { implicit request =>
    repo.findByDiretor(diretor).map { filmes =>
      if (filmes.isEmpty)
        NotFound
      else Ok(filmes.map(createFilmeResponse)
        .map(filmeToHtml).mkString("<ul><li>", "</li><br/><li>", "</li></ul>")).as(HTML)
    }
  }
  def listByGenero(genero: String): Action[AnyContent] = Action.async { implicit request =>
    repo.findByGenero(genero).map{ filmes =>
      if (filmes.isEmpty)
        NotFound
      else Ok(filmes.map(createFilmeResponse)
        .map(filmeToHtml).mkString("<ul><li>", "</li><br/><li>", "</li></ul>")).as(HTML)
    }
  }
  def listByAno(anoStr: String): Action[AnyContent] = Action.async { implicit request =>
    Try(anoStr.toInt).fold(
      _ => Future(BadRequest("Insira um ano válido.")),
      ano => repo.findByAno(ano).map{ filmes =>
        if (filmes.isEmpty)
          NotFound
        else Ok(filmes.map(createFilmeResponse)
          .map(filmeToHtml)
          .mkString("<ul><li>", "</li><br/><li>", "</li></ul>")).as(HTML)
      }
    )
  }
  def delete(idStr: String): Action[AnyContent] = Action.async { implicit request =>
    Try(idStr.toLong).fold(
      _ => Future(BadRequest("Insira um ID válido.")),
      id => repo.delete(id).map { filmeDeletado =>
//      NoContent // é o status ideal para informar que um recurso foi apagado com sucesso
        Ok(filmeToXml(createFilmeResponse(filmeDeletado)))
      }.recover {
        case _: NotFoundException =>
          NotFound(s"Filme de ID [ $id ] não encontrado.")
      }
    )
  }

  // POST /
  def create: Action[AnyContent] = Action.async{ implicit request =>
    CreateFilmeForm.form.bindFromRequest.fold(
      withErrors => Future(BadRequest(withErrors.errorsAsJson)),
      form => {
        repo.create(form.titulo, form.diretor, form.estudio, form.genero, form.ano).map { filme =>
          Created(Json.toJson(createFilmeResponse(filme)))
        }.recover {
          case _: DuplicateKeyException =>
            Conflict(s"Já existe um filme com o título informado.")
          case e: Exception =>
            Logger.error(s"[ FilmesController - create ] ${e.getMessage}")
            InternalServerError("Ocorreu um erro ao criar o filme.")
        }
      }
    )
  }

  // PUT /{id}/{titulo}
  def update(idStr: String, titulo: String): Action[AnyContent] = Action.async { implicit request =>
    Try(idStr.toLong).fold(
      _ => Future(BadRequest("Insira um ano válido.")),
      id => repo.update(id, titulo).map(f =>
        Ok(Json.toJson(createFilmeResponse(f)))
      ).recover {
        case _: NotFoundException =>
          NotFound(s"Filme de id [ $id ] não encontrado.")
        case _: DuplicateKeyException =>
          Conflict(s"Já existe um filme com o título [ $titulo ]")
        case _ =>
          InternalServerError("Ocorreu um erro ao atualizar.")
      }
    )
  }

  private def filmeToXml(filme: FilmeResponse): Elem = {
    <message>
      <id>{filme.id}</id>
      <titulo>{filme.titulo}</titulo>
      <diretor>{filme.diretor}</diretor>
      <estudio>{filme.estudio}</estudio>
      <genero>{filme.genero}</genero>
      <ano>{filme.ano}</ano>
      <link>{filme.link}</link>
    </message>
  }

  private def filmeToHtml(filme: FilmeResponse) = {
    s"""<div>
       |<strong>ID: </strong>${filme.id}<br/>
       |<strong>Titulo: </strong>${filme.titulo}<br/>
       |<strong>Diretor: </strong>${filme.diretor}<br/>
       |<strong>Estudio: </strong>${filme.estudio}<br/>
       |<strong>Genero: </strong>${filme.genero}<br/>
       |<strong>Ano: </strong>${filme.ano}<br/>
       |<strong>Link: </strong>${filme.link}<br/>
       |</div>
     """.stripMargin
  }

  private def createFilmeResponse(f: Filme) =
    FilmeResponse(f.id, f.titulo, f.diretor, f.estudio, f.genero, f.ano, filmesRouter.get().link(f.id))
}