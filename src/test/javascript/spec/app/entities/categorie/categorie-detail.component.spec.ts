import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GestionevenementappTestModule } from '../../../test.module';
import { CategorieDetailComponent } from 'app/entities/categorie/categorie-detail.component';
import { Categorie } from 'app/shared/model/categorie.model';

describe('Component Tests', () => {
  describe('Categorie Management Detail Component', () => {
    let comp: CategorieDetailComponent;
    let fixture: ComponentFixture<CategorieDetailComponent>;
    const route = ({ data: of({ categorie: new Categorie(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GestionevenementappTestModule],
        declarations: [CategorieDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(CategorieDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CategorieDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load categorie on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.categorie).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
