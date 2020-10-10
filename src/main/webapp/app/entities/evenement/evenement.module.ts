import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GestionevenementappSharedModule } from 'app/shared/shared.module';
import { EvenementComponent } from './evenement.component';
import { EvenementDetailComponent } from './evenement-detail.component';
import { EvenementUpdateComponent } from './evenement-update.component';
import { EvenementDeleteDialogComponent } from './evenement-delete-dialog.component';
import { EvenementParticipantComponent } from './evenement-participant/evenement-participant.component';
import { evenementRoute } from './evenement.route';

@NgModule({
  imports: [GestionevenementappSharedModule, RouterModule.forChild(evenementRoute)],
  declarations: [EvenementComponent, EvenementDetailComponent, EvenementUpdateComponent, EvenementDeleteDialogComponent, EvenementParticipantComponent],
  entryComponents: [EvenementDeleteDialogComponent],
})
export class GestionevenementappEvenementModule {}
