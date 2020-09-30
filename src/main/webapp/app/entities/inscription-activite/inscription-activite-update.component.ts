import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IInscriptionActivite, InscriptionActivite } from 'app/shared/model/inscription-activite.model';
import { InscriptionActiviteService } from './inscription-activite.service';
import { IActivite } from 'app/shared/model/activite.model';
import { ActiviteService } from 'app/entities/activite/activite.service';
import { IParticipant } from 'app/shared/model/participant.model';
import { ParticipantService } from 'app/entities/participant/participant.service';

type SelectableEntity = IActivite | IParticipant;

@Component({
  selector: 'jhi-inscription-activite-update',
  templateUrl: './inscription-activite-update.component.html',
})
export class InscriptionActiviteUpdateComponent implements OnInit {
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
    protected inscriptionActiviteService: InscriptionActiviteService,
    protected activiteService: ActiviteService,
    protected participantService: ParticipantService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inscriptionActivite }) => {
      this.updateForm(inscriptionActivite);

      this.activiteService.query().subscribe((res: HttpResponse<IActivite[]>) => (this.activites = res.body || []));

      this.participantService.query().subscribe((res: HttpResponse<IParticipant[]>) => (this.participants = res.body || []));
    });
  }

  updateForm(inscriptionActivite: IInscriptionActivite): void {
    this.editForm.patchValue({
      id: inscriptionActivite.id,
      role: inscriptionActivite.role,
      activiteId: inscriptionActivite.activiteId,
      participantId: inscriptionActivite.participantId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const inscriptionActivite = this.createFromForm();
    if (inscriptionActivite.id !== undefined) {
      this.subscribeToSaveResponse(this.inscriptionActiviteService.update(inscriptionActivite));
    } else {
      this.subscribeToSaveResponse(this.inscriptionActiviteService.create(inscriptionActivite));
    }
  }

  private createFromForm(): IInscriptionActivite {
    return {
      ...new InscriptionActivite(),
      id: this.editForm.get(['id'])!.value,
      role: this.editForm.get(['role'])!.value,
      activiteId: this.editForm.get(['activiteId'])!.value,
      participantId: this.editForm.get(['participantId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInscriptionActivite>>): void {
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
