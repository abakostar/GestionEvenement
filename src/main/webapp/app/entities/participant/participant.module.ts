import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GestionevenementappSharedModule } from 'app/shared/shared.module';
import { ParticipantComponent } from './participant.component';
import { ParticipantDetailComponent } from './participant-detail.component';
import { ParticipantUpdateComponent } from './participant-update.component';
import { ParticipantDeleteDialogComponent } from './participant-delete-dialog.component';
import { participantRoute } from './participant.route';
import { InscriptionEvenementComponent } from './inscription-evenement/inscription-evenement.component';

@NgModule({
  imports: [GestionevenementappSharedModule, RouterModule.forChild(participantRoute)],
  declarations: [
    ParticipantComponent,
    ParticipantDetailComponent,
    ParticipantUpdateComponent,
    ParticipantDeleteDialogComponent,
    InscriptionEvenementComponent,
  ],
  entryComponents: [ParticipantDeleteDialogComponent],
})
export class GestionevenementappParticipantModule {}
