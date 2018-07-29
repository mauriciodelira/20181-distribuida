import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppComponent} from './app.component';
import {FormsModule} from "@angular/forms";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {SearchFilmeComponent} from './components/search-filme/search-filme.component';
import {ApiResponseComponent} from "./components/api-response/api-response.component";
import { FilmesListComponent } from './components/filmes-list/filmes-list.component';
import { FilmesListItemComponent } from './components/filmes-list-item/filmes-list-item.component';
import { FilmeDetailComponent } from './components/filme-detail/filme-detail.component';
import {RouterModule, Routes} from "@angular/router";
import { InicioComponent } from './components/inicio/inicio.component';
import {ResponseInterceptor} from "./interceptors/response-interceptor";
import {NovoFilmeComponent} from "./components/new-filme/novo-filme.component";

const appRoutes: Routes = [
  { path: '', component: InicioComponent },
  { path: 'filme/:id/:type', component: FilmeDetailComponent },
  { path: 'novo', component: NovoFilmeComponent },
];

@NgModule({
  declarations: [
    AppComponent,
    SearchFilmeComponent,
    ApiResponseComponent,
    FilmesListComponent,
    FilmesListItemComponent,
    FilmeDetailComponent,
    InicioComponent,
    NovoFilmeComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    RouterModule.forRoot(
      appRoutes,
    )
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ResponseInterceptor,
      multi: true,
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
