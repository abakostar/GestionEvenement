import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GestionevenementappTestModule } from '../../../test.module';
import { EmplacementDetailComponent } from 'app/entities/emplacement/emplacement-detail.component';
import { Emplacement } from 'app/shared/model/emplacement.model';

describe('Component Tests', () => {
  describe('Emplacement Management Detail Component', () => {
    let comp: EmplacementDetailComponent;
    let fixture: ComponentFixture<EmplacementDetailComponent>;
    const route = ({ data: of({ emplacement: new Emplacement(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GestionevenementappTestModule],
        declarations: [EmplacementDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(EmplacementDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EmplacementDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load emplacement on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.emplacement).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
