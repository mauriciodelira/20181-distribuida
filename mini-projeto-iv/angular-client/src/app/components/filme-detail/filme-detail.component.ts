import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {FilmesApiService} from "../../services/filmes-api.service";
import {Filme} from "../../models/Filme";
import {HttpErrorHandlerService} from "../../services/http-error-handler.service";
import {ApiResponseService} from "../../services/api-response.service";

@Component({
  selector: 'app-filme-detail',
  templateUrl: './filme-detail.component.html',
  styleUrls: ['./filme-detail.component.css']
})
export class FilmeDetailComponent implements OnInit {

  filmeId = undefined;
  filmeType = 'json';
  selectedFilme: Filme = undefined;
  novoTitulo: string;

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

  atualizarFilme() {
    this.filmesApi.updateFilme(this.filmeId, this.novoTitulo).subscribe(filme => {
      this.selectedFilme = filme;
      },
      err =>
        this.errorHandler.handle(err)
    )
  }

  deletarFilme() {
    if (confirm(`Tem certeza de que deseja deletar o filme "${this.selectedFilme.titulo}"?`)) {
      this.filmesApi.deleteFilme(this.filmeId).subscribe( f => {
        // this.apiResponseService.setApiResponse(
        //   `id:${f.id};titulo:${f.titulo};diretor:${f.diretor};genero:${f.genero};estudio:${f.estudio};ano:${f.ano}`);
        alert("Filme deletado com sucesso! Voltando à página inicial...");
        this.router.navigate(['/']);
      }, err => {
        console.log(err);
        this.apiResponseService.setApiResponse(`
        > Status: ${err.status}<br/>
> Corpo da resposta:<br/>${err.error.text}`)
        alert("Filme deletado com sucesso! Voltando à página inicial...");
        this.router.navigate(['/']);
      })
    }
  }

  ngOnInit() {
    this.filmeId = this.route.snapshot.paramMap.get('id');
    this.filmeType = this.route.snapshot.paramMap.get('type');

    this.filmesApi.listById(Number(this.filmeId), this.filmeType).subscribe(filme => {
        this.selectedFilme = filme;
      }
    )
  }

}
