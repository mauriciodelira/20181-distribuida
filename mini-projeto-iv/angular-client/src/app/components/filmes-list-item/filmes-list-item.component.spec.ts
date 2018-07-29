import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FilmesListItemComponent } from './filmes-list-item.component';

describe('FilmesListItemComponent', () => {
  let component: FilmesListItemComponent;
  let fixture: ComponentFixture<FilmesListItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FilmesListItemComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FilmesListItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
