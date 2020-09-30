import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IInscriptionEvenement, InscriptionEvenement } from 'app/shared/model/inscription-evenement.model';
import { InscriptionEvenementService } from './inscription-evenement.service';
import { IEvenement } from 'app/shared/model/evenement.model';
import { EvenementService } from 'app/entities/evenement/evenement.service';
import { IParticipant } from 'app/shared/model/participant.model';
import { ParticipantService } from 'app/entities/participant/participant.service';

type SelectableEntity = IEvenement | IParticipant;

@Component({
  selector: 'jhi-inscription-evenement-update',
  templateUrl: './inscription-evenement-update.component.html',
})
export class InscriptionEvenementUpdateComponent implements OnInit {
  isSaving = false;
  evenements: IEvenement[] = [];
  participants: IParticipant[] = [];

  editForm = this.fb.group({
    id: [],
    loginParticipant: [],
    passwordParticipant: [],
    evenementId: [null, Validators.required],
    participantId: [],
  });

  constructor(
    protected inscriptionEvenementService: InscriptionEvenementService,
    protected evenementService: EvenementService,
    protected participantService: ParticipantService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inscriptionEvenement }) => {
      this.updateForm(inscriptionEvenement);

      this.evenementService.query().subscribe((res: HttpResponse<IEvenement[]>) => (this.evenements = res.body || []));

      this.participantService.query().subscribe((res: HttpResponse<IParticipant[]>) => (this.participants = res.body || []));
    });
  }

  updateForm(inscriptionEvenement: IInscriptionEvenement): void {
    this.editForm.patchValue({
      id: inscriptionEvenement.id,
      loginParticipant: inscriptionEvenement.loginParticipant,
      passwordParticipant: inscriptionEvenement.passwordParticipant,
      evenementId: inscriptionEvenement.evenementId,
      participantId: inscriptionEvenement.participantId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const inscriptionEvenement = this.createFromForm();
    if (inscriptionEvenement.id !== undefined) {
      this.subscribeToSaveResponse(this.inscriptionEvenementService.update(inscriptionEvenement));
    } else {
      this.subscribeToSaveResponse(this.inscriptionEvenementService.create(inscriptionEvenement));
    }
  }

  private createFromForm(): IInscriptionEvenement {
    return {
      ...new InscriptionEvenement(),
      id: this.editForm.get(['id'])!.value,
      loginParticipant: this.editForm.get(['loginParticipant'])!.value,
      passwordParticipant: this.editForm.get(['passwordParticipant'])!.value,
      evenementId: this.editForm.get(['evenementId'])!.value,
      participantId: this.editForm.get(['participantId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInscriptionEvenement>>): void {
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
