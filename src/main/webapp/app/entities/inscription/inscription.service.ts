import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IInscription } from 'app/shared/model/inscription.model';

type EntityResponseType = HttpResponse<IInscription>;
type EntityArrayResponseType = HttpResponse<IInscription[]>;

@Injectable({ providedIn: 'root' })
export class InscriptionService {
  public resourceUrl = SERVER_API_URL + 'api/inscriptions';

  constructor(protected http: HttpClient) {}

  create(inscription: IInscription): Observable<EntityResponseType> {
    return this.http.post<IInscription>(this.resourceUrl, inscription, { observe: 'response' });
  }

  update(inscription: IInscription): Observable<EntityResponseType> {
    return this.http.put<IInscription>(this.resourceUrl, inscription, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInscription>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInscription[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
