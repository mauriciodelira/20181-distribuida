package v1.filmes

import javax.inject.Inject
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

class FilmesRouter @Inject()(controller: FilmesController) extends SimpleRouter {
  val prefix = "/api/filmes"

  def link(id: Long): String = {
    import com.netaporter.uri.dsl._
    prefix.concat(s"/filme?id=$id&type=json")
  }

  override def routes: Routes = {
    case GET(p"/") => controller.index
    case GET(p"/filme") => controller.showById
    case GET(p"/titulo/$titulo") => controller.showByTitulo(titulo)
    case GET(p"/diretor/$diretor") => controller.listByDiretor(diretor)
    case GET(p"/genero/$genero") => controller.listByGenero(genero)
    case GET(p"/ano/$ano") => controller.listByAno(ano)
    case PUT(p"/atualizar/$id/$novoNome") => controller.update(id, novoNome)
    case POST(p"/") => controller.create
    case DELETE(p"/$id") => controller.delete(id)
  }

}