import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IEvenement, Evenement } from 'app/shared/model/evenement.model';
import { EvenementService } from './evenement.service';
import { ICategorie } from 'app/shared/model/categorie.model';
import { CategorieService } from 'app/entities/categorie/categorie.service';

@Component({
  selector: 'jhi-evenement-update',
  templateUrl: './evenement-update.component.html',
})
export class EvenementUpdateComponent implements OnInit {
  isSaving = false;
  categories: ICategorie[] = [];

  editForm = this.fb.group({
    id: [],
    code: [],
    dateDebut: [],
    dateFin: [],
    description: [],
    categorieId: [],
  });

  constructor(
    protected evenementService: EvenementService,
    protected categorieService: CategorieService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ evenement }) => {
      if (!evenement.id) {
        const today = moment().startOf('day');
        evenement.dateDebut = today;
        evenement.dateFin = today;
      }

      this.updateForm(evenement);

      this.categorieService.query().subscribe((res: HttpResponse<ICategorie[]>) => (this.categories = res.body || []));
    });
  }

  updateForm(evenement: IEvenement): void {
    this.editForm.patchValue({
      id: evenement.id,
      code: evenement.code,
      dateDebut: evenement.dateDebut ? evenement.dateDebut.format(DATE_TIME_FORMAT) : null,
      dateFin: evenement.dateFin ? evenement.dateFin.format(DATE_TIME_FORMAT) : null,
      description: evenement.description,
      categorieId: evenement.categorieId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const evenement = this.createFromForm();
    if (evenement.id !== undefined) {
      this.subscribeToSaveResponse(this.evenementService.update(evenement));
    } else {
      this.subscribeToSaveResponse(this.evenementService.create(evenement));
    }
  }

  private createFromForm(): IEvenement {
    return {
      ...new Evenement(),
      id: this.editForm.get(['id'])!.value,
      code: this.editForm.get(['code'])!.value,
      dateDebut: this.editForm.get(['dateDebut'])!.value ? moment(this.editForm.get(['dateDebut'])!.value, DATE_TIME_FORMAT) : undefined,
      dateFin: this.editForm.get(['dateFin'])!.value ? moment(this.editForm.get(['dateFin'])!.value, DATE_TIME_FORMAT) : undefined,
      description: this.editForm.get(['description'])!.value,
      categorieId: this.editForm.get(['categorieId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEvenement>>): void {
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

  trackById(index: number, item: ICategorie): any {
    return item.id;
  }
}
