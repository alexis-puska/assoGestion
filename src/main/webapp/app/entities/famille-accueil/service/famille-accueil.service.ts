import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFamilleAccueil, getFamilleAccueilIdentifier, FamilleAccueil } from '../famille-accueil.model';

export type EntityResponseType = HttpResponse<IFamilleAccueil>;
export type EntityArrayResponseType = HttpResponse<IFamilleAccueil[]>;

@Injectable({ providedIn: 'root' })
export class FamilleAccueilService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/famille-accueils');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(familleAccueil: IFamilleAccueil): Observable<EntityResponseType> {
    return this.http.post<IFamilleAccueil>(this.resourceUrl, familleAccueil, { observe: 'response' });
  }

  update(familleAccueil: IFamilleAccueil): Observable<EntityResponseType> {
    return this.http.put<IFamilleAccueil>(`${this.resourceUrl}/${getFamilleAccueilIdentifier(familleAccueil) as number}`, familleAccueil, {
      observe: 'response',
    });
  }

  partialUpdate(familleAccueil: IFamilleAccueil): Observable<EntityResponseType> {
    return this.http.patch<IFamilleAccueil>(
      `${this.resourceUrl}/${getFamilleAccueilIdentifier(familleAccueil) as number}`,
      familleAccueil,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFamilleAccueil>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFamilleAccueil[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  findFamilleAccueilAutocomplete(query: string): Observable<FamilleAccueil[]> {
    let queryParam = new HttpParams();
    queryParam = queryParam.append('query', query);
    return this.http.get<FamilleAccueil[]>(`${this.resourceUrl}/autocomplete`, {
      params: queryParam,
    });
  }

  addFamilleAccueilToCollectionIfMissing(
    familleAccueilCollection: IFamilleAccueil[],
    ...familleAccueilsToCheck: (IFamilleAccueil | null | undefined)[]
  ): IFamilleAccueil[] {
    const familleAccueils: IFamilleAccueil[] = familleAccueilsToCheck.filter(isPresent);
    if (familleAccueils.length > 0) {
      const familleAccueilCollectionIdentifiers = familleAccueilCollection.map(
        familleAccueilItem => getFamilleAccueilIdentifier(familleAccueilItem)!
      );
      const familleAccueilsToAdd = familleAccueils.filter(familleAccueilItem => {
        const familleAccueilIdentifier = getFamilleAccueilIdentifier(familleAccueilItem);
        if (familleAccueilIdentifier == null || familleAccueilCollectionIdentifiers.includes(familleAccueilIdentifier)) {
          return false;
        }
        familleAccueilCollectionIdentifiers.push(familleAccueilIdentifier);
        return true;
      });
      return [...familleAccueilsToAdd, ...familleAccueilCollection];
    }
    return familleAccueilCollection;
  }
}
