import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IVille, Ville } from 'app/shared/model/ville.model';
import { VilleService } from './ville.service';
import { IPays } from 'app/shared/model/pays.model';
import { PaysService } from 'app/entities/pays/pays.service';

@Component({
  selector: 'jhi-ville-update',
  templateUrl: './ville-update.component.html',
})
export class VilleUpdateComponent implements OnInit {
  isSaving = false;
  pays: IPays[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [],
    paysId: [],
  });

  constructor(
    protected villeService: VilleService,
    protected paysService: PaysService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ville }) => {
      this.updateForm(ville);

      this.paysService.query().subscribe((res: HttpResponse<IPays[]>) => (this.pays = res.body || []));
    });
  }

  updateForm(ville: IVille): void {
    this.editForm.patchValue({
      id: ville.id,
      nom: ville.nom,
      paysId: ville.paysId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ville = this.createFromForm();
    if (ville.id !== undefined) {
      this.subscribeToSaveResponse(this.villeService.update(ville));
    } else {
      this.subscribeToSaveResponse(this.villeService.create(ville));
    }
  }

  private createFromForm(): IVille {
    return {
      ...new Ville(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      paysId: this.editForm.get(['paysId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVille>>): void {
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

  trackById(index: number, item: IPays): any {
    return item.id;
  }
}
