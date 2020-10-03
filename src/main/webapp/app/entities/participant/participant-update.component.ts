import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IParticipant, Participant } from 'app/shared/model/participant.model';
import { ParticipantService } from './participant.service';
import { IVille } from 'app/shared/model/ville.model';
import { VilleService } from 'app/entities/ville/ville.service';
import { IEvenement } from 'app/shared/model/evenement.model';
import { EvenementService } from 'app/entities/evenement/evenement.service';

type SelectableEntity = IVille | IEvenement;

@Component({
  selector: 'jhi-participant-update',
  templateUrl: './participant-update.component.html',
})
export class ParticipantUpdateComponent implements OnInit {
  isSaving = false;
  villes: IVille[] = [];
  evenements: IEvenement[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [],
    sexe: [],
    telephone: [],
    email: [],
    villeId: [],
    evenements: [],
  });

  constructor(
    protected participantService: ParticipantService,
    protected villeService: VilleService,
    protected evenementService: EvenementService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ participant }) => {
      this.updateForm(participant);

      this.villeService.query().subscribe((res: HttpResponse<IVille[]>) => (this.villes = res.body || []));

      this.evenementService.query().subscribe((res: HttpResponse<IEvenement[]>) => (this.evenements = res.body || []));
    });
  }

  updateForm(participant: IParticipant): void {
    this.editForm.patchValue({
      id: participant.id,
      nom: participant.nom,
      sexe: participant.sexe,
      telephone: participant.telephone,
      email: participant.email,
      villeId: participant.villeId,
      evenements: participant.evenements,
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
      nom: this.editForm.get(['nom'])!.value,
      sexe: this.editForm.get(['sexe'])!.value,
      telephone: this.editForm.get(['telephone'])!.value,
      email: this.editForm.get(['email'])!.value,
      villeId: this.editForm.get(['villeId'])!.value,
      evenements: this.editForm.get(['evenements'])!.value,
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

  getSelected(selectedVals: IEvenement[], option: IEvenement): IEvenement {
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
