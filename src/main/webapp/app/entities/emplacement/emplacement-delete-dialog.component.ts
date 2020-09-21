import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IEmplacement } from 'app/shared/model/emplacement.model';
import { EmplacementService } from './emplacement.service';

@Component({
  templateUrl: './emplacement-delete-dialog.component.html',
})
export class EmplacementDeleteDialogComponent {
  emplacement?: IEmplacement;

  constructor(
    protected emplacementService: EmplacementService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.emplacementService.delete(id).subscribe(() => {
      this.eventManager.broadcast('emplacementListModification');
      this.activeModal.close();
    });
  }
}
