import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IInscriptionEvenement } from 'app/shared/model/inscription-evenement.model';
import { InscriptionEvenementService } from './inscription-evenement.service';

@Component({
  templateUrl: './inscription-evenement-delete-dialog.component.html',
})
export class InscriptionEvenementDeleteDialogComponent {
  inscriptionEvenement?: IInscriptionEvenement;

  constructor(
    protected inscriptionEvenementService: InscriptionEvenementService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.inscriptionEvenementService.delete(id).subscribe(() => {
      this.eventManager.broadcast('inscriptionEvenementListModification');
      this.activeModal.close();
    });
  }
}
