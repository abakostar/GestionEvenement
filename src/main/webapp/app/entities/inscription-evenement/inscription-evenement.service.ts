import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IInscriptionEvenement } from 'app/shared/model/inscription-evenement.model';

type EntityResponseType = HttpResponse<IInscriptionEvenement>;
type EntityArrayResponseType = HttpResponse<IInscriptionEvenement[]>;

@Injectable({ providedIn: 'root' })
export class InscriptionEvenementService {
  public resourceUrl = SERVER_API_URL + 'api/inscription-evenements';

  constructor(protected http: HttpClient) {}

  create(inscriptionEvenement: IInscriptionEvenement): Observable<EntityResponseType> {
    return this.http.post<IInscriptionEvenement>(this.resourceUrl, inscriptionEvenement, { observe: 'response' });
  }

  update(inscriptionEvenement: IInscriptionEvenement): Observable<EntityResponseType> {
    return this.http.put<IInscriptionEvenement>(this.resourceUrl, inscriptionEvenement, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInscriptionEvenement>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInscriptionEvenement[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
