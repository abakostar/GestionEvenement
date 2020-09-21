import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPays, Pays } from 'app/shared/model/pays.model';
import { PaysService } from './pays.service';

@Component({
  selector: 'jhi-pays-update',
  templateUrl: './pays-update.component.html',
})
export class PaysUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    code: [],
    nom: [],
  });

  constructor(protected paysService: PaysService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pays }) => {
      this.updateForm(pays);
    });
  }

  updateForm(pays: IPays): void {
    this.editForm.patchValue({
      id: pays.id,
      code: pays.code,
      nom: pays.nom,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pays = this.createFromForm();
    if (pays.id !== undefined) {
      this.subscribeToSaveResponse(this.paysService.update(pays));
    } else {
      this.subscribeToSaveResponse(this.paysService.create(pays));
    }
  }

  private createFromForm(): IPays {
    return {
      ...new Pays(),
      id: this.editForm.get(['id'])!.value,
      code: this.editForm.get(['code'])!.value,
      nom: this.editForm.get(['nom'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPays>>): void {
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
}
