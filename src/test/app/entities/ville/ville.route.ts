import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IVille, Ville } from 'app/shared/model/ville.model';
import { VilleService } from './ville.service';
import { VilleComponent } from './ville.component';
import { VilleDetailComponent } from './ville-detail.component';
import { VilleUpdateComponent } from './ville-update.component';

@Injectable({ providedIn: 'root' })
export class VilleResolve implements Resolve<IVille> {
  constructor(private service: VilleService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVille> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((ville: HttpResponse<Ville>) => {
          if (ville.body) {
            return of(ville.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Ville());
  }
}

export const villeRoute: Routes = [
  {
    path: '',
    component: VilleComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gestionevenementappApp.ville.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VilleDetailComponent,
    resolve: {
      ville: VilleResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gestionevenementappApp.ville.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VilleUpdateComponent,
    resolve: {
      ville: VilleResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gestionevenementappApp.ville.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VilleUpdateComponent,
    resolve: {
      ville: VilleResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gestionevenementappApp.ville.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
