import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GestionevenementappTestModule } from '../../../test.module';
import { EmplacementUpdateComponent } from 'app/entities/emplacement/emplacement-update.component';
import { EmplacementService } from 'app/entities/emplacement/emplacement.service';
import { Emplacement } from 'app/shared/model/emplacement.model';

describe('Component Tests', () => {
  describe('Emplacement Management Update Component', () => {
    let comp: EmplacementUpdateComponent;
    let fixture: ComponentFixture<EmplacementUpdateComponent>;
    let service: EmplacementService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GestionevenementappTestModule],
        declarations: [EmplacementUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(EmplacementUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EmplacementUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EmplacementService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Emplacement(123);
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
        const entity = new Emplacement();
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
