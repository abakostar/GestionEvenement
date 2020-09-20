import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IEvenement } from 'app/shared/model/evenement.model';
import { EvenementService } from './evenement.service';

@Component({
  templateUrl: './evenement-delete-dialog.component.html',
})
export class EvenementDeleteDialogComponent {
  evenement?: IEvenement;

  constructor(protected evenementService: EvenementService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.evenementService.delete(id).subscribe(() => {
      this.eventManager.broadcast('evenementListModification');
      this.activeModal.close();
    });
  }
}
