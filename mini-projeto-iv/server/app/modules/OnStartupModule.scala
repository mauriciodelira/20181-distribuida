package modules

import javax.inject.{Inject, Singleton}
import com.google.inject.AbstractModule
import repositories.FilmeRepository
import play.api.Logger

import scala.concurrent.ExecutionContext
import scala.util.Success

class OnStartupModule extends AbstractModule {

  override def configure(): Unit = {
    Logger.info("Abrindo a vitrine de filmes...")
    bind(classOf[InitFilmes]).asEagerSingleton()
  }

}

@Singleton
class InitFilmes @Inject()(implicit ec: ExecutionContext, repo: FilmeRepository) {
  Logger.info("Organizando filmes...")
  repo.list().onComplete{
    case Success(filmes) =>
      if(!filmes.exists(_.titulo.equalsIgnoreCase("Titanic"))) {
        Logger.info("Adicionando \"Titanic\" à prateleira...")
        repo.create("Titanic", "James Cameron", "Paramount", "Drama", 1997)
      }
      if(!filmes.exists(_.titulo.equalsIgnoreCase("Ocean's 8"))) {
        Logger.info("Adicionando \"Ocean's 8\" à prateleira...")
        repo.create("Ocean's 8", "Gary Ross", "Warner Bros. Pictures", "Mistério", 2018)
      }
      if(!filmes.exists(_.titulo.equalsIgnoreCase("Paris is burning"))) {
        Logger.info("Adicionando \"Paris is Burning\" à prateleira...")
        repo.create("Paris is Burning", "Jennie Livingston", "Academy Entertainment", "Documentário", 1990)
      }
      if(!filmes.exists(_.titulo.equalsIgnoreCase("Rei Leão"))) {
        Logger.info("Adicionando \"Rei Leão\" à prateleira...")
        repo.create("Rei Leão", "Rob Minkoff", "Walt Disney Animation Studios", "Drama", 1994)
      }
  }

  Logger.info("Prateleira de filmes organizada com sucesso.")
}