import {Component, OnInit} from '@angular/core';
import {Filme} from "../../models/Filme";
import {FilmesService} from "../../services/filmes.service";
import {FilmesApiService} from "../../services/filmes-api.service";

@Component({
  selector: 'app-filmes-list',
  templateUrl: './filmes-list.component.html',
  styleUrls: ['./filmes-list.component.css']
})
export class FilmesListComponent implements OnInit {
  filmes: Array<Filme>;

  constructor(
    private filmesService: FilmesService,
    private filmesApi: FilmesApiService,
  ) {
  }

  ngOnInit() {
    this.filmesService.filmes.subscribe(filmes => {
      this.filmes = filmes;
    });

    this.filmesApi.listAll().subscribe(filmes => {
      this.filmesService.setFilmes(filmes);
    });
  }

}
