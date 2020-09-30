import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IInscriptionActivite } from 'app/shared/model/inscription-activite.model';

type EntityResponseType = HttpResponse<IInscriptionActivite>;
type EntityArrayResponseType = HttpResponse<IInscriptionActivite[]>;

@Injectable({ providedIn: 'root' })
export class InscriptionActiviteService {
  public resourceUrl = SERVER_API_URL + 'api/inscription-activites';

  constructor(protected http: HttpClient) {}

  create(inscriptionActivite: IInscriptionActivite): Observable<EntityResponseType> {
    return this.http.post<IInscriptionActivite>(this.resourceUrl, inscriptionActivite, { observe: 'response' });
  }

  update(inscriptionActivite: IInscriptionActivite): Observable<EntityResponseType> {
    return this.http.put<IInscriptionActivite>(this.resourceUrl, inscriptionActivite, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInscriptionActivite>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInscriptionActivite[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
