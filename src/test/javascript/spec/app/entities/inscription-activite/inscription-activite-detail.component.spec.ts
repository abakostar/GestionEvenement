import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GestionevenementappTestModule } from '../../../test.module';
import { InscriptionActiviteDetailComponent } from 'app/entities/inscription-activite/inscription-activite-detail.component';
import { InscriptionActivite } from 'app/shared/model/inscription-activite.model';

describe('Component Tests', () => {
  describe('InscriptionActivite Management Detail Component', () => {
    let comp: InscriptionActiviteDetailComponent;
    let fixture: ComponentFixture<InscriptionActiviteDetailComponent>;
    const route = ({ data: of({ inscriptionActivite: new InscriptionActivite(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GestionevenementappTestModule],
        declarations: [InscriptionActiviteDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(InscriptionActiviteDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(InscriptionActiviteDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load inscriptionActivite on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.inscriptionActivite).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
