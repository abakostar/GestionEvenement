import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IInscription } from 'app/shared/model/inscription.model';
import { InscriptionService } from './inscription.service';

@Component({
  templateUrl: './inscription-delete-dialog.component.html',
})
export class InscriptionDeleteDialogComponent {
  inscription?: IInscription;

  constructor(
    protected inscriptionService: InscriptionService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.inscriptionService.delete(id).subscribe(() => {
      this.eventManager.broadcast('inscriptionListModification');
      this.activeModal.close();
    });
  }
}
