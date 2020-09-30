import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'categorie',
        loadChildren: () => import('./categorie/categorie.module').then(m => m.GestionevenementappCategorieModule),
      },
      {
        path: 'evenement',
        loadChildren: () => import('./evenement/evenement.module').then(m => m.GestionevenementappEvenementModule),
      },
      {
        path: 'pays',
        loadChildren: () => import('./pays/pays.module').then(m => m.GestionevenementappPaysModule),
      },
      {
        path: 'ville',
        loadChildren: () => import('./ville/ville.module').then(m => m.GestionevenementappVilleModule),
      },
      {
        path: 'emplacement',
        loadChildren: () => import('./emplacement/emplacement.module').then(m => m.GestionevenementappEmplacementModule),
      },
      {
        path: 'activite',
        loadChildren: () => import('./activite/activite.module').then(m => m.GestionevenementappActiviteModule),
      },
      {
        path: 'participant',
        loadChildren: () => import('./participant/participant.module').then(m => m.GestionevenementappParticipantModule),
      },
      {
        path: 'inscription-evenement',
        loadChildren: () =>
          import('./inscription-evenement/inscription-evenement.module').then(m => m.GestionevenementappInscriptionEvenementModule),
      },
      {
        path: 'inscription-activite',
        loadChildren: () =>
          import('./inscription-activite/inscription-activite.module').then(m => m.GestionevenementappInscriptionActiviteModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class GestionevenementappEntityModule {}
