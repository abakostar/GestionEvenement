import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IInscription, Inscription } from 'app/shared/model/inscription.model';
import { InscriptionService } from './inscription.service';
import { IActivite } from 'app/shared/model/activite.model';
import { ActiviteService } from 'app/entities/activite/activite.service';
import { IParticipant } from 'app/shared/model/participant.model';
import { ParticipantService } from 'app/entities/participant/participant.service';

type SelectableEntity = IActivite | IParticipant;

@Component({
  selector: 'jhi-inscription-update',
  templateUrl: './inscription-update.component.html',
})
export class InscriptionUpdateComponent implements OnInit {
  isSaving = false;
  activites: IActivite[] = [];
  participants: IParticipant[] = [];

  editForm = this.fb.group({
    id: [],
    role: [],
    activiteId: [],
    participantId: [],
  });

  constructor(
    protected inscriptionService: InscriptionService,
    protected activiteService: ActiviteService,
    protected participantService: ParticipantService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inscription }) => {
      this.updateForm(inscription);

      this.activiteService.query().subscribe((res: HttpResponse<IActivite[]>) => (this.activites = res.body || []));

      this.participantService.query().subscribe((res: HttpResponse<IParticipant[]>) => (this.participants = res.body || []));
    });
  }

  updateForm(inscription: IInscription): void {
    this.editForm.patchValue({
      id: inscription.id,
      role: inscription.role,
      activiteId: inscription.activiteId,
      participantId: inscription.participantId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const inscription = this.createFromForm();
    if (inscription.id !== undefined) {
      this.subscribeToSaveResponse(this.inscriptionService.update(inscription));
    } else {
      this.subscribeToSaveResponse(this.inscriptionService.create(inscription));
    }
  }

  private createFromForm(): IInscription {
    return {
      ...new Inscription(),
      id: this.editForm.get(['id'])!.value,
      role: this.editForm.get(['role'])!.value,
      activiteId: this.editForm.get(['activiteId'])!.value,
      participantId: this.editForm.get(['participantId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInscription>>): void {
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
