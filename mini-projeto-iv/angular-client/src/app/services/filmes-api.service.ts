import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {FilmesService} from "./filmes.service";
import {environment} from "../../environments/environment";
import {Filme} from "../models/Filme";
import {Observable} from "rxjs/internal/Observable";
import {map} from "rxjs/operators";
import * as xml2js from 'xml2js';
import {ApiResponseService} from "./api-response.service";

@Injectable({
  providedIn: 'root'
})
export class FilmesApiService {
  private baseURL = `${environment.baseApiURL}`;

  constructor(
    private httpClient: HttpClient,
    private apiResponse: ApiResponseService,
  ) {
  }

  private buildFilmeFromResponse(resp: Object): Filme {
    let filme = new Filme();
    filme.id = resp['id'];
    filme.titulo = resp['titulo'];
    filme.genero = resp['genero'];
    filme.estudio = resp['estudio'];
    filme.diretor = resp['diretor'];
    filme.ano = resp['ano'];
    filme.link = resp['link'];
    return filme;
  }

  /**
   * Realiza uma consulta à API, retornando
   * uma lista de filmes em JSON
   */
  listAll(): Observable<Array<Filme>> {
    return this.httpClient.get<Array<Filme>>(this.baseURL);
  }

  private parseNumbers(value: string) {
    const parsed = value.match(/^\d+$/);
    if (parsed && parsed.length > 0) {
      return +value;
    } else {
      return value;
    }
  }

  /**
   * Realiza uma consulta à API passando o ID e o tipo
   * de retorno nos parâmetros, retornando um filme no
   * formato solicitado (XML ou JSON. Por padrão: JSON)
   * 200 OK ou 404 Not Found.
   */
  listById(id: number, type: string): Observable<Filme> {
    const params = new HttpParams().set('id', id.toString())
      .set('type', type);

    return this.httpClient.get(
      `${this.baseURL}/filme`,
      {
        params: params,
        responseType: "text",
      }).pipe(map(res => {

        if (type.toLowerCase() == 'xml') {
          let resposta: Filme = undefined;
          xml2js.Parser({
            explicitRoot: false,
            explicitArray: false,
            valueProcessors: [this.parseNumbers],
          }).parseString(res, (err, res) => {
            resposta = this.buildFilmeFromResponse(res);
          });
          return resposta;
        } else {
          return this.buildFilmeFromResponse(JSON.parse(res));
        }
      }
    ));
  }

  /**
   * Realiza uma consulta à API passando na URL o
   * Título, retornando um único filme em HTML em
   * 200 OK ou 404 not found.
   */
  listByTituloHTML(titulo: string): Observable<string> {
    return this.httpClient.get(
      `${this.baseURL}/titulo/${titulo}`,
      {responseType: "text"},
    );
  }

  /**
   * Realiza uma consulta à API passando na URL o
   * diretor, retornando uma lista de filmes em HTML
   * em 200 OK ou 404 Not Found
   */
  listByDiretorHTML(diretor: String): Observable<string> {
    return this.httpClient.get(
      `${this.baseURL}/diretor/${diretor}`,
      {responseType: "text"},
    );
  }

  /**
   * Realiza uma consulta à API passando na URL o
   * gênero, retornando uma lista de filmes em HTML
   * em 200 OK ou 404 Not Found
   */
  listByGeneroHTML(genero: String): Observable<string> {
    return this.httpClient.get(
      `${this.baseURL}/genero/${genero}`,
      {responseType: "text"},
    );
  }

  /**
   * Realiza uma consulta à API passando na URL o
   * ano, retornando uma lista de filmes em HTML
   * em 200 OK ou 404 Not Found.
   */
  listByAnoHTML(ano: number): Observable<string> {
    return this.httpClient.get(
      `${this.baseURL}/ano/${ano}`,
      {responseType: "text"},
    );
  }

  /**
   * Realiza uma requisição PUT passando o ID do filme
   * a ser atualizado e o novo título ambos pela URL,
   * retornando 200 OK e o filme atualizado em JSON,
   * 404 Not Found ou 409 Conflict.
   */
  updateFilme(id: number, novoTitulo: string): Observable<Filme> {
    return this.httpClient.put<Filme>(
      `${this.baseURL}/atualizar/${id}/${novoTitulo}`,
      null
    )
  }

  /**
   * Cria um novo recurso Filme, retornando 200
   * e o recurso em JSON ou então 409 Conflict
   * caso já exista um com aquele título
   */
  createFilme(filme: Filme): Observable<Filme> {
    return this.httpClient.post<Filme>(
      `${this.baseURL}`,
      {
        titulo: filme.titulo,
        ano: filme.ano,
        genero: filme.genero,
        estudio: filme.estudio,
        diretor: filme.diretor,
      }
    );
  }

  /**
   * Apaga recurso daquele ID do serviço, e
   * retorna um XML com o recurso apagado (200)
   * ou 404 não encontrado
   * @param {number} id ID do filme a ser apagado
   */
  deleteFilme(id: number): Observable<Filme> {
    return this.httpClient.delete<Filme>(
      `${this.baseURL}/${id}`
    ).pipe(map(res => {
        let resposta: Filme = undefined;
        xml2js.Parser({
          explicitRoot: false,
          explicitArray: false,
          valueProcessors: [this.parseNumbers],
        }).parseString(res, (err, res) => {
          resposta = this.buildFilmeFromResponse(res);
        });
        return resposta;
      }
    ));
  }

}
