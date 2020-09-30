import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IInscriptionActivite } from 'app/shared/model/inscription-activite.model';
import { InscriptionActiviteService } from './inscription-activite.service';

@Component({
  templateUrl: './inscription-activite-delete-dialog.component.html',
})
export class InscriptionActiviteDeleteDialogComponent {
  inscriptionActivite?: IInscriptionActivite;

  constructor(
    protected inscriptionActiviteService: InscriptionActiviteService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.inscriptionActiviteService.delete(id).subscribe(() => {
      this.eventManager.broadcast('inscriptionActiviteListModification');
      this.activeModal.close();
    });
  }
}
