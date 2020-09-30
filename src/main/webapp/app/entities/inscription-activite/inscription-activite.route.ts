import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IInscriptionActivite, InscriptionActivite } from 'app/shared/model/inscription-activite.model';
import { InscriptionActiviteService } from './inscription-activite.service';
import { InscriptionActiviteComponent } from './inscription-activite.component';
import { InscriptionActiviteDetailComponent } from './inscription-activite-detail.component';
import { InscriptionActiviteUpdateComponent } from './inscription-activite-update.component';

@Injectable({ providedIn: 'root' })
export class InscriptionActiviteResolve implements Resolve<IInscriptionActivite> {
  constructor(private service: InscriptionActiviteService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInscriptionActivite> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((inscriptionActivite: HttpResponse<InscriptionActivite>) => {
          if (inscriptionActivite.body) {
            return of(inscriptionActivite.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new InscriptionActivite());
  }
}

export const inscriptionActiviteRoute: Routes = [
  {
    path: '',
    component: InscriptionActiviteComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gestionevenementappApp.inscriptionActivite.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InscriptionActiviteDetailComponent,
    resolve: {
      inscriptionActivite: InscriptionActiviteResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gestionevenementappApp.inscriptionActivite.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InscriptionActiviteUpdateComponent,
    resolve: {
      inscriptionActivite: InscriptionActiviteResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gestionevenementappApp.inscriptionActivite.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InscriptionActiviteUpdateComponent,
    resolve: {
      inscriptionActivite: InscriptionActiviteResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gestionevenementappApp.inscriptionActivite.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
