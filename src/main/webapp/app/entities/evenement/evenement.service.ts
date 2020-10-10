import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IEvenement } from 'app/shared/model/evenement.model';
import {IParticipantEvenement} from "../../shared/model/participant-evenement.model";

type EntityResponseType = HttpResponse<IEvenement>;
type EntityArrayResponseType = HttpResponse<IEvenement[]>;

@Injectable({ providedIn: 'root' })
export class EvenementService {
  public resourceUrl = SERVER_API_URL + 'api/evenements';

  constructor(protected http: HttpClient) {}

  create(evenement: IEvenement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(evenement);
    return this.http
      .post<IEvenement>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  addParticipant(participantEvenement: IParticipantEvenement): Observable<EntityResponseType> {
    const url = this.resourceUrl+"/addParticipant"
    return this.http
      .post<IParticipantEvenement>(url, participantEvenement, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(evenement: IEvenement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(evenement);
    return this.http
      .put<IEvenement>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEvenement>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEvenement[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(evenement: IEvenement): IEvenement {
    const copy: IEvenement = Object.assign({}, evenement, {
      dateDebut: evenement.dateDebut && evenement.dateDebut.isValid() ? evenement.dateDebut.toJSON() : undefined,
      dateFin: evenement.dateFin && evenement.dateFin.isValid() ? evenement.dateFin.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateDebut = res.body.dateDebut ? moment(res.body.dateDebut) : undefined;
      res.body.dateFin = res.body.dateFin ? moment(res.body.dateFin) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((evenement: IEvenement) => {
        evenement.dateDebut = evenement.dateDebut ? moment(evenement.dateDebut) : undefined;
        evenement.dateFin = evenement.dateFin ? moment(evenement.dateFin) : undefined;
      });
    }
    return res;
  }
}
