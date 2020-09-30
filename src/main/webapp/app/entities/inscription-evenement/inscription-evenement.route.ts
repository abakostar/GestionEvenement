import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IInscriptionEvenement, InscriptionEvenement } from 'app/shared/model/inscription-evenement.model';
import { InscriptionEvenementService } from './inscription-evenement.service';
import { InscriptionEvenementComponent } from './inscription-evenement.component';
import { InscriptionEvenementDetailComponent } from './inscription-evenement-detail.component';
import { InscriptionEvenementUpdateComponent } from './inscription-evenement-update.component';

@Injectable({ providedIn: 'root' })
export class InscriptionEvenementResolve implements Resolve<IInscriptionEvenement> {
  constructor(private service: InscriptionEvenementService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInscriptionEvenement> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((inscriptionEvenement: HttpResponse<InscriptionEvenement>) => {
          if (inscriptionEvenement.body) {
            return of(inscriptionEvenement.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new InscriptionEvenement());
  }
}

export const inscriptionEvenementRoute: Routes = [
  {
    path: '',
    component: InscriptionEvenementComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gestionevenementappApp.inscriptionEvenement.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InscriptionEvenementDetailComponent,
    resolve: {
      inscriptionEvenement: InscriptionEvenementResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gestionevenementappApp.inscriptionEvenement.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InscriptionEvenementUpdateComponent,
    resolve: {
      inscriptionEvenement: InscriptionEvenementResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gestionevenementappApp.inscriptionEvenement.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InscriptionEvenementUpdateComponent,
    resolve: {
      inscriptionEvenement: InscriptionEvenementResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gestionevenementappApp.inscriptionEvenement.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
