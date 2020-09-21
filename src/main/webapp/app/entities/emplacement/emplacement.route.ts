import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IEmplacement, Emplacement } from 'app/shared/model/emplacement.model';
import { EmplacementService } from './emplacement.service';
import { EmplacementComponent } from './emplacement.component';
import { EmplacementDetailComponent } from './emplacement-detail.component';
import { EmplacementUpdateComponent } from './emplacement-update.component';

@Injectable({ providedIn: 'root' })
export class EmplacementResolve implements Resolve<IEmplacement> {
  constructor(private service: EmplacementService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEmplacement> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((emplacement: HttpResponse<Emplacement>) => {
          if (emplacement.body) {
            return of(emplacement.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Emplacement());
  }
}

export const emplacementRoute: Routes = [
  {
    path: '',
    component: EmplacementComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gestionevenementappApp.emplacement.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EmplacementDetailComponent,
    resolve: {
      emplacement: EmplacementResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gestionevenementappApp.emplacement.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EmplacementUpdateComponent,
    resolve: {
      emplacement: EmplacementResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gestionevenementappApp.emplacement.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EmplacementUpdateComponent,
    resolve: {
      emplacement: EmplacementResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gestionevenementappApp.emplacement.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
