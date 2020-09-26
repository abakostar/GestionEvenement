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
  dateActiviteDp: any;

  editForm = this.fb.group({
    id: [],
    nom: [],
    description: [],
    etatclos: [],
    dateActivite: [],
    heureDebut: [],
    heureFin: [],
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
        activite.heureDebut = today;
        activite.heureFin = today;
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
      etatclos: activite.etatclos,
      dateActivite: activite.dateActivite,
      heureDebut: activite.heureDebut ? activite.heureDebut.format(DATE_TIME_FORMAT) : null,
      heureFin: activite.heureFin ? activite.heureFin.format(DATE_TIME_FORMAT) : null,
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
      etatclos: this.editForm.get(['etatclos'])!.value,
      dateActivite: this.editForm.get(['dateActivite'])!.value,
      heureDebut: this.editForm.get(['heureDebut'])!.value ? moment(this.editForm.get(['heureDebut'])!.value, DATE_TIME_FORMAT) : undefined,
      heureFin: this.editForm.get(['heureFin'])!.value ? moment(this.editForm.get(['heureFin'])!.value, DATE_TIME_FORMAT) : undefined,
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
