import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IVille } from 'app/shared/model/ville.model';
import { VilleService } from './ville.service';

@Component({
  templateUrl: './ville-delete-dialog.component.html',
})
export class VilleDeleteDialogComponent {
  ville?: IVille;

  constructor(protected villeService: VilleService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.villeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('villeListModification');
      this.activeModal.close();
    });
  }
}
