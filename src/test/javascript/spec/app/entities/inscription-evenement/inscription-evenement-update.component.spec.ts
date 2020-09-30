import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GestionevenementappTestModule } from '../../../test.module';
import { InscriptionEvenementUpdateComponent } from 'app/entities/inscription-evenement/inscription-evenement-update.component';
import { InscriptionEvenementService } from 'app/entities/inscription-evenement/inscription-evenement.service';
import { InscriptionEvenement } from 'app/shared/model/inscription-evenement.model';

describe('Component Tests', () => {
  describe('InscriptionEvenement Management Update Component', () => {
    let comp: InscriptionEvenementUpdateComponent;
    let fixture: ComponentFixture<InscriptionEvenementUpdateComponent>;
    let service: InscriptionEvenementService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GestionevenementappTestModule],
        declarations: [InscriptionEvenementUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(InscriptionEvenementUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InscriptionEvenementUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(InscriptionEvenementService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new InscriptionEvenement(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new InscriptionEvenement();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
