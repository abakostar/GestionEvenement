import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GestionevenementappTestModule } from '../../../test.module';
import { InscriptionActiviteUpdateComponent } from 'app/entities/inscription-activite/inscription-activite-update.component';
import { InscriptionActiviteService } from 'app/entities/inscription-activite/inscription-activite.service';
import { InscriptionActivite } from 'app/shared/model/inscription-activite.model';

describe('Component Tests', () => {
  describe('InscriptionActivite Management Update Component', () => {
    let comp: InscriptionActiviteUpdateComponent;
    let fixture: ComponentFixture<InscriptionActiviteUpdateComponent>;
    let service: InscriptionActiviteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GestionevenementappTestModule],
        declarations: [InscriptionActiviteUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(InscriptionActiviteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InscriptionActiviteUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(InscriptionActiviteService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new InscriptionActivite(123);
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
        const entity = new InscriptionActivite();
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
