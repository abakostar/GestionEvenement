import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GestionevenementappSharedModule } from 'app/shared/shared.module';
import { InscriptionEvenementComponent } from './inscription-evenement.component';
import { InscriptionEvenementDetailComponent } from './inscription-evenement-detail.component';
import { InscriptionEvenementUpdateComponent } from './inscription-evenement-update.component';
import { InscriptionEvenementDeleteDialogComponent } from './inscription-evenement-delete-dialog.component';
import { inscriptionEvenementRoute } from './inscription-evenement.route';

@NgModule({
  imports: [GestionevenementappSharedModule, RouterModule.forChild(inscriptionEvenementRoute)],
  declarations: [
    InscriptionEvenementComponent,
    InscriptionEvenementDetailComponent,
    InscriptionEvenementUpdateComponent,
    InscriptionEvenementDeleteDialogComponent,
  ],
  entryComponents: [InscriptionEvenementDeleteDialogComponent],
})
export class GestionevenementappInscriptionEvenementModule {}
