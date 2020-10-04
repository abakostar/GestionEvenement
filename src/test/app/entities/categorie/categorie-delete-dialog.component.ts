import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICategorie } from 'app/shared/model/categorie.model';
import { CategorieService } from './categorie.service';

@Component({
  templateUrl: './categorie-delete-dialog.component.html',
})
export class CategorieDeleteDialogComponent {
  categorie?: ICategorie;

  constructor(protected categorieService: CategorieService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.categorieService.delete(id).subscribe(() => {
      this.eventManager.broadcast('categorieListModification');
      this.activeModal.close();
    });
  }
}
