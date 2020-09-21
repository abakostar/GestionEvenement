import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IActivite } from 'app/shared/model/activite.model';

type EntityResponseType = HttpResponse<IActivite>;
type EntityArrayResponseType = HttpResponse<IActivite[]>;

@Injectable({ providedIn: 'root' })
export class ActiviteService {
  public resourceUrl = SERVER_API_URL + 'api/activites';

  constructor(protected http: HttpClient) {}

  create(activite: IActivite): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(activite);
    return this.http
      .post<IActivite>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(activite: IActivite): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(activite);
    return this.http
      .put<IActivite>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IActivite>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IActivite[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(activite: IActivite): IActivite {
    const copy: IActivite = Object.assign({}, activite, {
      date_activite: activite.date_activite && activite.date_activite.isValid() ? activite.date_activite.format(DATE_FORMAT) : undefined,
      heure_debut: activite.heure_debut && activite.heure_debut.isValid() ? activite.heure_debut.toJSON() : undefined,
      heure_fin: activite.heure_fin && activite.heure_fin.isValid() ? activite.heure_fin.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date_activite = res.body.date_activite ? moment(res.body.date_activite) : undefined;
      res.body.heure_debut = res.body.heure_debut ? moment(res.body.heure_debut) : undefined;
      res.body.heure_fin = res.body.heure_fin ? moment(res.body.heure_fin) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((activite: IActivite) => {
        activite.date_activite = activite.date_activite ? moment(activite.date_activite) : undefined;
        activite.heure_debut = activite.heure_debut ? moment(activite.heure_debut) : undefined;
        activite.heure_fin = activite.heure_fin ? moment(activite.heure_fin) : undefined;
      });
    }
    return res;
  }
}
