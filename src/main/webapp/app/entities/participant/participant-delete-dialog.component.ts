import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IParticipant } from 'app/shared/model/participant.model';
import { ParticipantService } from './participant.service';

@Component({
  templateUrl: './participant-delete-dialog.component.html',
})
export class ParticipantDeleteDialogComponent {
  participant?: IParticipant;

  constructor(
    protected participantService: ParticipantService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.participantService.delete(id).subscribe(() => {
      this.eventManager.broadcast('participantListModification');
      this.activeModal.close();
    });
  }
}
