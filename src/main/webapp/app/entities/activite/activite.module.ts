import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GestionevenementappSharedModule } from 'app/shared/shared.module';
import { ActiviteComponent } from './activite.component';
import { ActiviteDetailComponent } from './activite-detail.component';
import { ActiviteUpdateComponent } from './activite-update.component';
import { ActiviteDeleteDialogComponent } from './activite-delete-dialog.component';
import { activiteRoute } from './activite.route';
import { InscriptionActiviteComponent } from './inscription-activite/inscription-activite.component';

@NgModule({
  imports: [GestionevenementappSharedModule, RouterModule.forChild(activiteRoute)],
  declarations: [
    ActiviteComponent,
    ActiviteDetailComponent,
    ActiviteUpdateComponent,
    ActiviteDeleteDialogComponent,
    InscriptionActiviteComponent,
  ],
  entryComponents: [ActiviteDeleteDialogComponent],
})
export class GestionevenementappActiviteModule {}
