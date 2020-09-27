import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GestionevenementappTestModule } from '../../../test.module';
import { InscriptionDetailComponent } from 'app/entities/inscription/inscription-detail.component';
import { Inscription } from 'app/shared/model/inscription.model';

describe('Component Tests', () => {
  describe('Inscription Management Detail Component', () => {
    let comp: InscriptionDetailComponent;
    let fixture: ComponentFixture<InscriptionDetailComponent>;
    const route = ({ data: of({ inscription: new Inscription(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GestionevenementappTestModule],
        declarations: [InscriptionDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(InscriptionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(InscriptionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load inscription on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.inscription).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
