import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IEmplacement, Emplacement } from 'app/shared/model/emplacement.model';
import { EmplacementService } from './emplacement.service';
import { IVille } from 'app/shared/model/ville.model';
import { VilleService } from 'app/entities/ville/ville.service';

@Component({
  selector: 'jhi-emplacement-update',
  templateUrl: './emplacement-update.component.html',
})
export class EmplacementUpdateComponent implements OnInit {
  isSaving = false;
  villes: IVille[] = [];

  editForm = this.fb.group({
    id: [],
    code: [],
    description: [],
    capacite: [],
    villeId: [],
  });

  constructor(
    protected emplacementService: EmplacementService,
    protected villeService: VilleService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ emplacement }) => {
      this.updateForm(emplacement);

      this.villeService.query().subscribe((res: HttpResponse<IVille[]>) => (this.villes = res.body || []));
    });
  }

  updateForm(emplacement: IEmplacement): void {
    this.editForm.patchValue({
      id: emplacement.id,
      code: emplacement.code,
      description: emplacement.description,
      capacite: emplacement.capacite,
      villeId: emplacement.villeId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const emplacement = this.createFromForm();
    if (emplacement.id !== undefined) {
      this.subscribeToSaveResponse(this.emplacementService.update(emplacement));
    } else {
      this.subscribeToSaveResponse(this.emplacementService.create(emplacement));
    }
  }

  private createFromForm(): IEmplacement {
    return {
      ...new Emplacement(),
      id: this.editForm.get(['id'])!.value,
      code: this.editForm.get(['code'])!.value,
      description: this.editForm.get(['description'])!.value,
      capacite: this.editForm.get(['capacite'])!.value,
      villeId: this.editForm.get(['villeId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmplacement>>): void {
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

  trackById(index: number, item: IVille): any {
    return item.id;
  }
}
