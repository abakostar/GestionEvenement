import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GestionevenementappTestModule } from '../../../test.module';
import { EvenementDetailComponent } from 'app/entities/evenement/evenement-detail.component';
import { Evenement } from 'app/shared/model/evenement.model';

describe('Component Tests', () => {
  describe('Evenement Management Detail Component', () => {
    let comp: EvenementDetailComponent;
    let fixture: ComponentFixture<EvenementDetailComponent>;
    const route = ({ data: of({ evenement: new Evenement(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GestionevenementappTestModule],
        declarations: [EvenementDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(EvenementDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EvenementDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load evenement on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.evenement).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
