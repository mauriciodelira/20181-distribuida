import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {FilmesApiService} from "../../services/filmes-api.service";
import {Filme} from "../../models/Filme";
import {HttpErrorHandlerService} from "../../services/http-error-handler.service";
import {ApiResponseService} from "../../services/api-response.service";

@Component({
  selector: 'app-novo-filme',
  templateUrl: './novo-filme.component.html',
})
export class NovoFilmeComponent {

  filmeId = undefined;
  titulo: string;
  diretor: string;
  ano: number;
  estudio: string;
  genero: string;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private filmesApi: FilmesApiService,
    private errorHandler: HttpErrorHandlerService,
    private apiResponseService: ApiResponseService,
  ) {
  }

  gotoHome() {
    this.router.navigate(['/']);
  }

  cadastrarFilme() {
    let filme: Filme = new Filme();
    filme.ano = this.ano;
    filme.titulo = this.titulo;
    filme.estudio = this.estudio;
    filme.diretor = this.diretor;
    filme.genero = this.genero;
    this.filmesApi.createFilme(filme).subscribe(filme => {
      if(filme.id) {
        alert('Filme criado com sucesso!');
        this.router.navigate(['/filme', filme.id, 'json']);
      } else {
        alert('Ocorreu um erro ao criar seu filme. Tente novamente mais tarde.');
      }
      },
      err => {
        alert(`Ocorreu um erro. Por favor, revise os campos e tente novamente.\nErros:\n\n${JSON.stringify(err.errors)}`);
        this.errorHandler.handle(err);
      }
    )
  }
}
