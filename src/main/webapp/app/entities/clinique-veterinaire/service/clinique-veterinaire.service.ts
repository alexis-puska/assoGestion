import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICliniqueVeterinaire, getCliniqueVeterinaireIdentifier, CliniqueVeterinaire } from '../clinique-veterinaire.model';

export type EntityResponseType = HttpResponse<ICliniqueVeterinaire>;
export type EntityArrayResponseType = HttpResponse<ICliniqueVeterinaire[]>;

@Injectable({ providedIn: 'root' })
export class CliniqueVeterinaireService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/clinique-veterinaires');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cliniqueVeterinaire: ICliniqueVeterinaire): Observable<EntityResponseType> {
    return this.http.post<ICliniqueVeterinaire>(this.resourceUrl, cliniqueVeterinaire, { observe: 'response' });
  }

  update(cliniqueVeterinaire: ICliniqueVeterinaire): Observable<EntityResponseType> {
    return this.http.put<ICliniqueVeterinaire>(
      `${this.resourceUrl}/${getCliniqueVeterinaireIdentifier(cliniqueVeterinaire) as number}`,
      cliniqueVeterinaire,
      { observe: 'response' }
    );
  }

  partialUpdate(cliniqueVeterinaire: ICliniqueVeterinaire): Observable<EntityResponseType> {
    return this.http.patch<ICliniqueVeterinaire>(
      `${this.resourceUrl}/${getCliniqueVeterinaireIdentifier(cliniqueVeterinaire) as number}`,
      cliniqueVeterinaire,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICliniqueVeterinaire>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICliniqueVeterinaire[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  findCliniqueVeterinaireAutocomplete(query: string): Observable<CliniqueVeterinaire[]> {
    let queryParam = new HttpParams();
    queryParam = queryParam.append('query', query);
    return this.http.get<CliniqueVeterinaire[]>(`${this.resourceUrl}/autocomplete`, {
      params: queryParam,
    });
  }

  addCliniqueVeterinaireToCollectionIfMissing(
    cliniqueVeterinaireCollection: ICliniqueVeterinaire[],
    ...cliniqueVeterinairesToCheck: (ICliniqueVeterinaire | null | undefined)[]
  ): ICliniqueVeterinaire[] {
    const cliniqueVeterinaires: ICliniqueVeterinaire[] = cliniqueVeterinairesToCheck.filter(isPresent);
    if (cliniqueVeterinaires.length > 0) {
      const cliniqueVeterinaireCollectionIdentifiers = cliniqueVeterinaireCollection.map(
        cliniqueVeterinaireItem => getCliniqueVeterinaireIdentifier(cliniqueVeterinaireItem)!
      );
      const cliniqueVeterinairesToAdd = cliniqueVeterinaires.filter(cliniqueVeterinaireItem => {
        const cliniqueVeterinaireIdentifier = getCliniqueVeterinaireIdentifier(cliniqueVeterinaireItem);
        if (cliniqueVeterinaireIdentifier == null || cliniqueVeterinaireCollectionIdentifiers.includes(cliniqueVeterinaireIdentifier)) {
          return false;
        }
        cliniqueVeterinaireCollectionIdentifiers.push(cliniqueVeterinaireIdentifier);
        return true;
      });
      return [...cliniqueVeterinairesToAdd, ...cliniqueVeterinaireCollection];
    }
    return cliniqueVeterinaireCollection;
  }
}
