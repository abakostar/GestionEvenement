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
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

type SelectableEntity = IVille | IUser;

@Component({
  selector: 'jhi-participant-update',
  templateUrl: './participant-update.component.html',
})
export class ParticipantUpdateComponent implements OnInit {
  isSaving = false;
  villes: IVille[] = [];
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [],
    sexe: [],
    telephone: [],
    email: [],
    villeResidenceId: [],
    userId: [],
  });

  constructor(
    protected participantService: ParticipantService,
    protected villeService: VilleService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ participant }) => {
      this.updateForm(participant);

      this.villeService.query().subscribe((res: HttpResponse<IVille[]>) => (this.villes = res.body || []));

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(participant: IParticipant): void {
    this.editForm.patchValue({
      id: participant.id,
      nom: participant.nom,
      sexe: participant.sexe,
      telephone: participant.telephone,
      email: participant.email,
      villeResidenceId: participant.villeResidenceId,
      userId: participant.userId,
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
      villeResidenceId: this.editForm.get(['villeResidenceId'])!.value,
      userId: this.editForm.get(['userId'])!.value,
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
}
