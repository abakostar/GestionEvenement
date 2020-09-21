import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IActivite } from 'app/shared/model/activite.model';
import { ActiviteService } from './activite.service';

@Component({
  templateUrl: './activite-delete-dialog.component.html',
})
export class ActiviteDeleteDialogComponent {
  activite?: IActivite;

  constructor(protected activiteService: ActiviteService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.activiteService.delete(id).subscribe(() => {
      this.eventManager.broadcast('activiteListModification');
      this.activeModal.close();
    });
  }
}
