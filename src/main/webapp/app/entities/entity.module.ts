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
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class GestionevenementappEntityModule {}
