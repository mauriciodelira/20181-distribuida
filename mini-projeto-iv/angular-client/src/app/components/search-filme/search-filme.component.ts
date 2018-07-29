import {Component, OnInit} from '@angular/core';
import {SearchFilterOption} from "../../models/SearchFilterOption";
import {FilmesApiService} from "../../services/filmes-api.service";
import {ApiResponseService} from "../../services/api-response.service";
import {HttpErrorHandlerService} from "../../services/http-error-handler.service";

@Component({
  selector: 'app-search-filme',
  templateUrl: './search-filme.component.html',
})
export class SearchFilmeComponent implements OnInit {

  optionValues: Array<SearchFilterOption> = [];
  htmlResponse: string = undefined;

  searchText: string;
  searchField: string;


  constructor(
    private filmesApi: FilmesApiService,
    private apiService: ApiResponseService,
    private httpErrorHandler: HttpErrorHandlerService,
  ) {
    this.optionValues = [
      {value: 'titulo', label: 'Título'},
      {value: 'diretor', label: 'Diretor'},
      {value: 'genero', label: 'Gênero'},
      {value: 'ano', label: 'Ano'}
    ];
    this.searchField = this.optionValues[0].value;
  }

  searchFor() {
    if (this.searchText && this.searchField) {
      switch (this.searchField.toLowerCase()) {
        case 'titulo':
          this.filmesApi.listByTituloHTML(this.searchText).subscribe(resp => {
              // this.apiService.setApiResponse(resp);
            },
            err => {
              this.httpErrorHandler.handle(err);
            });
          break;
        case 'diretor':
          this.filmesApi.listByDiretorHTML(this.searchText).subscribe(resp => {
            // this.apiService.setApiResponse(resp);
          }, err =>
              this.httpErrorHandler.handle(err)
            );
          break;
        case 'genero':
          this.filmesApi.listByGeneroHTML(this.searchText).subscribe(resp => {
            // this.apiService.setApiResponse(resp);
          },
            err => {
              this.httpErrorHandler.handle(err);
            });
          break;
        case 'ano':
          if (this.searchText.match(/\d/)) {
            this.filmesApi.listByAnoHTML(Number(this.searchText)).subscribe(resp => {
              // this.apiService.setApiResponse(resp);
            },
            err =>
              this.httpErrorHandler.handle(err)
            );
          } else {
            alert(`Valor para ano inválido. Valor buscado: ${this.searchText}.`);
          }
          break;
      }
    } else {
      alert('Preencha algo na pesquisa');
    }
  }

  ngOnInit() {
  }

}
