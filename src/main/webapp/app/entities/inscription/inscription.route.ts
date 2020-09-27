import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IInscription, Inscription } from 'app/shared/model/inscription.model';
import { InscriptionService } from './inscription.service';
import { InscriptionComponent } from './inscription.component';
import { InscriptionDetailComponent } from './inscription-detail.component';
import { InscriptionUpdateComponent } from './inscription-update.component';

@Injectable({ providedIn: 'root' })
export class InscriptionResolve implements Resolve<IInscription> {
  constructor(private service: InscriptionService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInscription> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((inscription: HttpResponse<Inscription>) => {
          if (inscription.body) {
            return of(inscription.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Inscription());
  }
}

export const inscriptionRoute: Routes = [
  {
    path: '',
    component: InscriptionComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gestionevenementappApp.inscription.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InscriptionDetailComponent,
    resolve: {
      inscription: InscriptionResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gestionevenementappApp.inscription.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InscriptionUpdateComponent,
    resolve: {
      inscription: InscriptionResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gestionevenementappApp.inscription.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InscriptionUpdateComponent,
    resolve: {
      inscription: InscriptionResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gestionevenementappApp.inscription.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
