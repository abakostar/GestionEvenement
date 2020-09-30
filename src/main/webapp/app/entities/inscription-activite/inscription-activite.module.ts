import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GestionevenementappSharedModule } from 'app/shared/shared.module';
import { InscriptionActiviteComponent } from './inscription-activite.component';
import { InscriptionActiviteDetailComponent } from './inscription-activite-detail.component';
import { InscriptionActiviteUpdateComponent } from './inscription-activite-update.component';
import { InscriptionActiviteDeleteDialogComponent } from './inscription-activite-delete-dialog.component';
import { inscriptionActiviteRoute } from './inscription-activite.route';

@NgModule({
  imports: [GestionevenementappSharedModule, RouterModule.forChild(inscriptionActiviteRoute)],
  declarations: [
    InscriptionActiviteComponent,
    InscriptionActiviteDetailComponent,
    InscriptionActiviteUpdateComponent,
    InscriptionActiviteDeleteDialogComponent,
  ],
  entryComponents: [InscriptionActiviteDeleteDialogComponent],
})
export class GestionevenementappInscriptionActiviteModule {}
