import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GestionevenementappTestModule } from '../../../test.module';
import { VilleDetailComponent } from 'app/entities/ville/ville-detail.component';
import { Ville } from 'app/shared/model/ville.model';

describe('Component Tests', () => {
  describe('Ville Management Detail Component', () => {
    let comp: VilleDetailComponent;
    let fixture: ComponentFixture<VilleDetailComponent>;
    const route = ({ data: of({ ville: new Ville(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GestionevenementappTestModule],
        declarations: [VilleDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(VilleDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(VilleDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load ville on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.ville).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
