import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GestionevenementappTestModule } from '../../../test.module';
import { EvenementUpdateComponent } from 'app/entities/evenement/evenement-update.component';
import { EvenementService } from 'app/entities/evenement/evenement.service';
import { Evenement } from 'app/shared/model/evenement.model';

describe('Component Tests', () => {
  describe('Evenement Management Update Component', () => {
    let comp: EvenementUpdateComponent;
    let fixture: ComponentFixture<EvenementUpdateComponent>;
    let service: EvenementService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GestionevenementappTestModule],
        declarations: [EvenementUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(EvenementUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EvenementUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EvenementService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Evenement(123);
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
        const entity = new Evenement();
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
