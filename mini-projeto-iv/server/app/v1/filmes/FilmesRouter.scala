package v1.filmes

import javax.inject.Inject
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

class FilmesRouter @Inject()(controller: FilmesController) extends SimpleRouter {
  val prefix = "/api/filmes/"

  def link(id: Long): String = {
    import com.netaporter.uri.dsl._
    val url = prefix / id.toString
    url.toString
  }

  override def routes: Routes = {
    case GET(p"/") => controller.index
    case GET(p"/id/$mediaType") => controller.showById(mediaType) // TODO: deve ter o tipo da resposta na URL???
    case GET(p"/titulo/$titulo") => controller.showByTitulo(titulo)
    case GET(p"/diretor/$diretor") => controller.listByDiretor(diretor)
    case GET(p"/genero/$genero") => controller.listByGenero(genero)
    case GET(p"/ano/$ano") => controller.listByAno(ano)
    case PUT(p"/$id/$novoNome") => controller.update(id, novoNome)
    case POST(p"/") => controller.create
    case DELETE(p"/$id") => controller.delete(id)
  }

}