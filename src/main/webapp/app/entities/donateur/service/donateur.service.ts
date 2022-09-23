import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDonateur, getDonateurIdentifier } from '../donateur.model';

export type EntityResponseType = HttpResponse<IDonateur>;
export type EntityArrayResponseType = HttpResponse<IDonateur[]>;

@Injectable({ providedIn: 'root' })
export class DonateurService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/donateurs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(donateur: IDonateur): Observable<EntityResponseType> {
    return this.http.post<IDonateur>(this.resourceUrl, donateur, { observe: 'response' });
  }

  update(donateur: IDonateur): Observable<EntityResponseType> {
    return this.http.put<IDonateur>(`${this.resourceUrl}/${getDonateurIdentifier(donateur) as number}`, donateur, { observe: 'response' });
  }

  partialUpdate(donateur: IDonateur): Observable<EntityResponseType> {
    return this.http.patch<IDonateur>(`${this.resourceUrl}/${getDonateurIdentifier(donateur) as number}`, donateur, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDonateur>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDonateur[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDonateurToCollectionIfMissing(donateurCollection: IDonateur[], ...donateursToCheck: (IDonateur | null | undefined)[]): IDonateur[] {
    const donateurs: IDonateur[] = donateursToCheck.filter(isPresent);
    if (donateurs.length > 0) {
      const donateurCollectionIdentifiers = donateurCollection.map(donateurItem => getDonateurIdentifier(donateurItem)!);
      const donateursToAdd = donateurs.filter(donateurItem => {
        const donateurIdentifier = getDonateurIdentifier(donateurItem);
        if (donateurIdentifier == null || donateurCollectionIdentifiers.includes(donateurIdentifier)) {
          return false;
        }
        donateurCollectionIdentifiers.push(donateurIdentifier);
        return true;
      });
      return [...donateursToAdd, ...donateurCollection];
    }
    return donateurCollection;
  }
}
