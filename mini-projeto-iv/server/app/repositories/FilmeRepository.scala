package repositories

import javax.inject.{Inject, Singleton}
import models.{DuplicateKeyException, Filme, NotFoundException}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class FilmeRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  // We want the JdbcProfile for this provider
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  // These imports are important, the first one brings db into scope, which will let you do the actual db operations.
  // The second one brings the Slick DSL into scope, which lets you define the table and other queries.
  import dbConfig._
  import profile.api._

  private class Filmes(tag: Tag) extends Table[Filme](tag, "Filmes") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def titulo = column[String]("titulo")

    def diretor = column[String]("diretor")

    def estudio = column[String]("estudio")

    def genero = column[String]("genero")

    def ano = column[Int]("ano")

    def * = (id, titulo, diretor, estudio, genero, ano) <> ((Filme.apply _).tupled, Filme.unapply)

  }

  /**
    * The starting point for all queries on the user table.
    */
  private val table = TableQuery[Filmes]

  def create(titulo: String,
             diretor: String,
             estudio: String,
             genero: String,
             ano: Int,
            ): Future[Filme] = db.run {
    for {
      exists <- table.filter(_.titulo like titulo).exists.result
      filme: Filme <- if (exists) throw DuplicateKeyException("Título já cadastrado")
      else {
//      prepara a query, mapeando as colunas que vou inserir os dados
        table.map(p => (p.titulo, p.diretor, p.estudio, p.genero, p.ano))
//        informo qual vai ser o retorno da query
          .returning(table.map(_.id))
//        mapeia os valores inseridos (em tuplas) e o retornado para uma entidade
          .into((fields, returnValue) => Filme(returnValue, fields._1, fields._2, fields._3, fields._4, fields._5))
//        insere os valores à query preparada, pulando colunas autogeradas
          .+=(titulo, diretor, estudio, genero, ano)
      }
    } yield filme
  }

  def list(): Future[Seq[Filme]] = db.run {
    table.result
  }

  // parametrizado na query
  def findById(id: Long): Future[Option[Filme]] = db.run {
    table.filter(_.id === id)
      .result.headOption
  }

  // daqui pra baixo, parametrizado na URL
  def findByTitulo(titulo: String): Future[Option[Filme]] = db.run {
    table.filter(_.titulo.toLowerCase like titulo.toLowerCase).result.headOption
  }

  def findByAno(ano: Int): Future[Seq[Filme]] = db.run {
    table.filter(_.ano === ano).result
  }

  def findByDiretor(diretor: String): Future[Seq[Filme]] = db.run {
    table.filter(_.diretor.toLowerCase like diretor.toLowerCase).result
  }

  def findByGenero(genero: String): Future[Seq[Filme]] = db.run {
    table.filter(_.genero.toLowerCase like genero.toLowerCase).result
  }

  def update(id: Long, novoTitulo: String): Future[Filme] = {
    db.run {
      for {
        existsId <- table.filter(_.id === id).exists.result
        existsTitle <- table.filter(_.titulo.toLowerCase === novoTitulo.toLowerCase).exists.result

        affectedRows <- if (existsId) {
          if (!existsTitle) {
            table.filter(_.id === id)
              .map(_.titulo)
              .update(novoTitulo)
          } else throw DuplicateKeyException("Título já cadastrado")
        } else throw NotFoundException("Não encontrado")

        result: Filme <- affectedRows match {
          case 0 => throw new Exception("Não foi possível atualizar o filme.")
          case _ => table.filter(_.id === id).result.head
        }
      } yield result
    }
  }

  def delete(id: Long): Future[Filme] = {
    findById(id).map { filmeAntigo =>
      if (filmeAntigo.isEmpty)
        throw NotFoundException(s"Filme de ID [ $id ] não encontrado.")

      db.run {
        table.filter(_.id === id).delete
      }

      filmeAntigo.get
    }
  }
}