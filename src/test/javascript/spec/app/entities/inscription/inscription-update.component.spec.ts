import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GestionevenementappTestModule } from '../../../test.module';
import { InscriptionUpdateComponent } from 'app/entities/inscription/inscription-update.component';
import { InscriptionService } from 'app/entities/inscription/inscription.service';
import { Inscription } from 'app/shared/model/inscription.model';

describe('Component Tests', () => {
  describe('Inscription Management Update Component', () => {
    let comp: InscriptionUpdateComponent;
    let fixture: ComponentFixture<InscriptionUpdateComponent>;
    let service: InscriptionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GestionevenementappTestModule],
        declarations: [InscriptionUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(InscriptionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InscriptionUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(InscriptionService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Inscription(123);
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
        const entity = new Inscription();
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
