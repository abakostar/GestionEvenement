import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IParticipant } from 'app/shared/model/participant.model';
import {IParticipantEvenement} from "../../shared/model/participant-evenement.model";
import {IParticipantActivite} from "../../shared/model/participant-activite.model";
import * as moment from "moment";

type EntityResponseType = HttpResponse<IParticipant>;
type EntityArrayResponseType = HttpResponse<IParticipant[]>;

@Injectable({ providedIn: 'root' })
export class ParticipantService {
  public resourceUrl = SERVER_API_URL + 'api/participants';

  constructor(protected http: HttpClient) {}

  create(participant: IParticipant): Observable<EntityResponseType> {
    return this.http
      .post<IParticipant>(this.resourceUrl, participant, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => res));
  }

  update(participant: IParticipant): Observable<EntityResponseType> {
    return this.http
      .put<IParticipant>(this.resourceUrl, participant, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => res));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IParticipant>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => res));
  }

  findByCurrentUser(): Observable<EntityResponseType> {
    const url = this.resourceUrl+'/currentUser'
    return this.http
      .get<IParticipant>(url, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => res));
  }

  findActivitesByEvenementId(evenementId?: number): Observable<HttpResponse<IParticipantActivite[]>> {
    const url = this.resourceUrl+'/activite/'+evenementId
    return this.http
      .get<IParticipantActivite[]>(url, {observe: 'response' })
      .pipe(map((res: HttpResponse<IParticipantActivite[]>) => this.convertDateArrayFromServer(res)));
  }

  addParticipant(participantEvenement: IParticipantEvenement): Observable<EntityResponseType> {
    const url = this.resourceUrl+"/addParticipant"
    return this.http
      .post<IParticipantEvenement>(url, participantEvenement, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => res));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IParticipant[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => res));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateArrayFromServer(res:  HttpResponse<IParticipantActivite[]>): HttpResponse<IParticipantActivite[]> {
    if (res.body) {
      res.body.forEach((activite: IParticipantActivite) => {
        activite.activite.dateActivite = activite.activite.dateActivite ? moment(activite.activite.dateActivite) : undefined;
        activite.activite.heureDebut = activite.activite.heureDebut ? moment(activite.activite.heureDebut) : undefined;
        activite.activite.heureFin = activite.activite.heureFin ? moment(activite.activite.heureFin) : undefined;
      });
    }
    return res;
  }

}
