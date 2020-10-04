import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IEmplacement } from 'app/shared/model/emplacement.model';

type EntityResponseType = HttpResponse<IEmplacement>;
type EntityArrayResponseType = HttpResponse<IEmplacement[]>;

@Injectable({ providedIn: 'root' })
export class EmplacementService {
  public resourceUrl = SERVER_API_URL + 'api/emplacements';

  constructor(protected http: HttpClient) {}

  create(emplacement: IEmplacement): Observable<EntityResponseType> {
    return this.http.post<IEmplacement>(this.resourceUrl, emplacement, { observe: 'response' });
  }

  update(emplacement: IEmplacement): Observable<EntityResponseType> {
    return this.http.put<IEmplacement>(this.resourceUrl, emplacement, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEmplacement>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEmplacement[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
