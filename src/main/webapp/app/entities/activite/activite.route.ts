import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IActivite, Activite } from 'app/shared/model/activite.model';
import { ActiviteService } from './activite.service';
import { ActiviteComponent } from './activite.component';
import { ActiviteDetailComponent } from './activite-detail.component';
import { ActiviteUpdateComponent } from './activite-update.component';

@Injectable({ providedIn: 'root' })
export class ActiviteResolve implements Resolve<IActivite> {
  constructor(private service: ActiviteService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IActivite> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((activite: HttpResponse<Activite>) => {
          if (activite.body) {
            return of(activite.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Activite());
  }
}

export const activiteRoute: Routes = [
  {
    path: '',
    component: ActiviteComponent,
    data: {
      authorities: [Authority.USER, Authority.PARTICIPANT],
      pageTitle: 'gestionevenementappApp.activite.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ActiviteDetailComponent,
    resolve: {
      activite: ActiviteResolve,
    },
    data: {
      authorities: [Authority.USER, Authority.PARTICIPANT],
      pageTitle: 'gestionevenementappApp.activite.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ActiviteUpdateComponent,
    resolve: {
      activite: ActiviteResolve,
    },
    data: {
      authorities: [Authority.USER, Authority.PARTICIPANT],
      pageTitle: 'gestionevenementappApp.activite.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ActiviteUpdateComponent,
    resolve: {
      activite: ActiviteResolve,
    },
    data: {
      authorities: [Authority.USER, Authority.PARTICIPANT],
      pageTitle: 'gestionevenementappApp.activite.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
