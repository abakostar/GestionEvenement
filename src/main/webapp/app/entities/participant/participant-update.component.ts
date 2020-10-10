import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';

import { IParticipant, Participant } from 'app/shared/model/participant.model';
import { ParticipantService } from './participant.service';
import { IVille } from 'app/shared/model/ville.model';
import { VilleService } from 'app/entities/ville/ville.service';
import { IEvenement } from 'app/shared/model/evenement.model';
import { EvenementService } from 'app/entities/evenement/evenement.service';
import { IActivite } from 'app/shared/model/activite.model';
import { ActiviteService } from 'app/entities/activite/activite.service';

type SelectableEntity = IVille | IEvenement | IActivite;

type SelectableManyToManyEntity = IEvenement | IActivite;

@Component({
  selector: 'jhi-participant-update',
  templateUrl: './participant-update.component.html',
})
export class ParticipantUpdateComponent implements OnInit {
  isSaving = false;
  villes: IVille[] = [];
  evenements: IEvenement[] = [];
  activites: IActivite[] = [];

  editForm = this.fb.group({
    id: [],
    sexe: [],
    telephone: [],
    userId: [],
    login: [],
    firstName: [],
    lastName: [],
    email: ['', [Validators.minLength(5), Validators.maxLength(254), Validators.email]],
    activated: [],
    langKey: [],
    createdBy: [],
    createdDate: [],
    lastModifiedBy: [],
    lastModifiedDate: [],
    password: [],
    villeId: [],
    evenements: [],
    activites: [],
  });

  constructor(
    protected participantService: ParticipantService,
    protected villeService: VilleService,
    protected evenementService: EvenementService,
    protected activiteService: ActiviteService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ participant }) => {
      if (!participant.id) {
        const today = moment().startOf('day');
        participant.createdDate = today;
        participant.lastModifiedDate = today;
      }

      this.updateForm(participant);

      this.villeService.query().subscribe((res: HttpResponse<IVille[]>) => (this.villes = res.body || []));

      this.evenementService.query().subscribe((res: HttpResponse<IEvenement[]>) => (this.evenements = res.body || []));

      this.activiteService.query().subscribe((res: HttpResponse<IActivite[]>) => (this.activites = res.body || []));
    });
  }

  updateForm(participant: IParticipant): void {
    this.editForm.patchValue({
      id: participant.id,
      sexe: participant.sexe,
      telephone: participant.telephone,
      login: participant.login,
      firstName: participant.firstName,
      lastName: participant.lastName,
      email: participant.email,
      password: participant.password,
      villeId: participant.villeId,
      evenements: participant.evenements,
      activites: participant.activites,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const participant = this.createFromForm();
    if (participant.id !== undefined) {
      this.subscribeToSaveResponse(this.participantService.update(participant));
    } else {
      this.subscribeToSaveResponse(this.participantService.create(participant));
    }
  }

  private createFromForm(): IParticipant {
    return {
      ...new Participant(),
      id: this.editForm.get(['id'])!.value,
      sexe: this.editForm.get(['sexe'])!.value,
      telephone: this.editForm.get(['telephone'])!.value,
      login: this.editForm.get(['login'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      email: this.editForm.get(['email'])!.value,
      password: this.editForm.get(['password'])!.value,
      villeId: this.editForm.get(['villeId'])!.value,
      evenements: this.editForm.get(['evenements'])!.value,
      activites: this.editForm.get(['activites'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IParticipant>>): void {
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

  getSelected(selectedVals: SelectableManyToManyEntity[], option: SelectableManyToManyEntity): SelectableManyToManyEntity {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
