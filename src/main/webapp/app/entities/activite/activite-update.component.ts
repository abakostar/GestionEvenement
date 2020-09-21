import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IActivite, Activite } from 'app/shared/model/activite.model';
import { ActiviteService } from './activite.service';
import { IEvenement } from 'app/shared/model/evenement.model';
import { EvenementService } from 'app/entities/evenement/evenement.service';
import { IEmplacement } from 'app/shared/model/emplacement.model';
import { EmplacementService } from 'app/entities/emplacement/emplacement.service';

type SelectableEntity = IEvenement | IEmplacement;

@Component({
  selector: 'jhi-activite-update',
  templateUrl: './activite-update.component.html',
})
export class ActiviteUpdateComponent implements OnInit {
  isSaving = false;
  evenements: IEvenement[] = [];
  emplacements: IEmplacement[] = [];
  date_activiteDp: any;

  editForm = this.fb.group({
    id: [],
    nom: [],
    description: [],
    date_activite: [],
    heure_debut: [],
    heure_fin: [],
    etatclos: [],
    evenementId: [],
    emplacementId: [],
  });

  constructor(
    protected activiteService: ActiviteService,
    protected evenementService: EvenementService,
    protected emplacementService: EmplacementService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ activite }) => {
      if (!activite.id) {
        const today = moment().startOf('day');
        activite.heure_debut = today;
        activite.heure_fin = today;
      }

      this.updateForm(activite);

      this.evenementService.query().subscribe((res: HttpResponse<IEvenement[]>) => (this.evenements = res.body || []));

      this.emplacementService.query().subscribe((res: HttpResponse<IEmplacement[]>) => (this.emplacements = res.body || []));
    });
  }

  updateForm(activite: IActivite): void {
    this.editForm.patchValue({
      id: activite.id,
      nom: activite.nom,
      description: activite.description,
      date_activite: activite.date_activite,
      heure_debut: activite.heure_debut ? activite.heure_debut.format(DATE_TIME_FORMAT) : null,
      heure_fin: activite.heure_fin ? activite.heure_fin.format(DATE_TIME_FORMAT) : null,
      etatclos: activite.etatclos,
      evenementId: activite.evenementId,
      emplacementId: activite.emplacementId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const activite = this.createFromForm();
    if (activite.id !== undefined) {
      this.subscribeToSaveResponse(this.activiteService.update(activite));
    } else {
      this.subscribeToSaveResponse(this.activiteService.create(activite));
    }
  }

  private createFromForm(): IActivite {
    return {
      ...new Activite(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      description: this.editForm.get(['description'])!.value,
      date_activite: this.editForm.get(['date_activite'])!.value,
      heure_debut: this.editForm.get(['heure_debut'])!.value
        ? moment(this.editForm.get(['heure_debut'])!.value, DATE_TIME_FORMAT)
        : undefined,
      heure_fin: this.editForm.get(['heure_fin'])!.value ? moment(this.editForm.get(['heure_fin'])!.value, DATE_TIME_FORMAT) : undefined,
      etatclos: this.editForm.get(['etatclos'])!.value,
      evenementId: this.editForm.get(['evenementId'])!.value,
      emplacementId: this.editForm.get(['emplacementId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IActivite>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
