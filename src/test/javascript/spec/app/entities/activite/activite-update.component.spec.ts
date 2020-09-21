import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GestionevenementappTestModule } from '../../../test.module';
import { ActiviteUpdateComponent } from 'app/entities/activite/activite-update.component';
import { ActiviteService } from 'app/entities/activite/activite.service';
import { Activite } from 'app/shared/model/activite.model';

describe('Component Tests', () => {
  describe('Activite Management Update Component', () => {
    let comp: ActiviteUpdateComponent;
    let fixture: ComponentFixture<ActiviteUpdateComponent>;
    let service: ActiviteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GestionevenementappTestModule],
        declarations: [ActiviteUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ActiviteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ActiviteUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ActiviteService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Activite(123);
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
        const entity = new Activite();
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
