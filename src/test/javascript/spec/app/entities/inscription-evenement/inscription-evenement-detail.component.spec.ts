import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GestionevenementappTestModule } from '../../../test.module';
import { InscriptionEvenementDetailComponent } from 'app/entities/inscription-evenement/inscription-evenement-detail.component';
import { InscriptionEvenement } from 'app/shared/model/inscription-evenement.model';

describe('Component Tests', () => {
  describe('InscriptionEvenement Management Detail Component', () => {
    let comp: InscriptionEvenementDetailComponent;
    let fixture: ComponentFixture<InscriptionEvenementDetailComponent>;
    const route = ({ data: of({ inscriptionEvenement: new InscriptionEvenement(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GestionevenementappTestModule],
        declarations: [InscriptionEvenementDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(InscriptionEvenementDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(InscriptionEvenementDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load inscriptionEvenement on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.inscriptionEvenement).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
