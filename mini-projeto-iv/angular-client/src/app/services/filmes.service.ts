import { Injectable } from '@angular/core';
import {BehaviorSubject} from "rxjs/internal/BehaviorSubject";
import {Filme} from "../models/Filme";

@Injectable({
  providedIn: 'root'
})
export class FilmesService {
  private filmesSource: BehaviorSubject<Filme[]> = new BehaviorSubject(undefined);

  filmes = this.filmesSource.asObservable();

  setFilmes(filmes: Filme[]) {
    this.filmesSource.next(filmes);
  }
}
