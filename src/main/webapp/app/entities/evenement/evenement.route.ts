import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IEvenement, Evenement } from 'app/shared/model/evenement.model';
import { EvenementService } from './evenement.service';
import { EvenementComponent } from './evenement.component';
import { EvenementDetailComponent } from './evenement-detail.component';
import { EvenementUpdateComponent } from './evenement-update.component';
import { InscriptionEvenementComponent } from './inscription-evenement/inscription-evenement.component';

@Injectable({ providedIn: 'root' })
export class EvenementResolve implements Resolve<IEvenement> {
  constructor(private service: EvenementService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEvenement> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((evenement: HttpResponse<Evenement>) => {
          if (evenement.body) {
            return of(evenement.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Evenement());
  }
}

export const evenementRoute: Routes = [
  {
    path: '',
    component: EvenementComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gestionevenementappApp.evenement.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EvenementDetailComponent,
    resolve: {
      evenement: EvenementResolve,
    },
    data: {
      authorities: [Authority.USER, Authority.PARTICIPANT],
      pageTitle: 'gestionevenementappApp.evenement.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EvenementUpdateComponent,
    resolve: {
      evenement: EvenementResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gestionevenementappApp.evenement.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EvenementUpdateComponent,
    resolve: {
      evenement: EvenementResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gestionevenementappApp.evenement.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'inscriptionevent',
    component: InscriptionEvenementComponent,
    data: {
      authorities: [Authority.USER, Authority.PARTICIPANT],
      pageTitle: 'gestionevenementappApp.evenement.home.insriptionEvent',
    },
    canActivate: [UserRouteAccessService],
  },
];
