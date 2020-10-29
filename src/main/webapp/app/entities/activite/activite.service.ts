import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IActivite } from 'app/shared/model/activite.model';
import { IParticipantActivite } from 'app/shared/model/participant-activite.model';

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
      dateActivite: activite.dateActivite && activite.dateActivite.isValid() ? activite.dateActivite.format(DATE_FORMAT) : undefined,
      heureDebut: activite.heureDebut && activite.heureDebut.isValid() ? activite.heureDebut.toJSON() : undefined,
      heureFin: activite.heureFin && activite.heureFin.isValid() ? activite.heureFin.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateActivite = res.body.dateActivite ? moment(res.body.dateActivite) : undefined;
      res.body.heureDebut = res.body.heureDebut ? moment(res.body.heureDebut) : undefined;
      res.body.heureFin = res.body.heureFin ? moment(res.body.heureFin) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((activite: IActivite) => {
        activite.dateActivite = activite.dateActivite ? moment(activite.dateActivite) : undefined;
        activite.heureDebut = activite.heureDebut ? moment(activite.heureDebut) : undefined;
        activite.heureFin = activite.heureFin ? moment(activite.heureFin) : undefined;
      });
    }
    return res;
  }

  addParticipantActivite(participantActivite: IParticipantActivite): Observable<EntityResponseType> {
    const url = this.resourceUrl + '/addParticipant';
    return this.http
      .post<IParticipantActivite>(url, participantActivite, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  testPlacedispo(id: number): Observable<HttpResponse<Boolean>> {
    const url = this.resourceUrl + '/placedispo';
    return this.http.get<Boolean>(`${url}/${id}`, { observe: 'response' });
  }
}
