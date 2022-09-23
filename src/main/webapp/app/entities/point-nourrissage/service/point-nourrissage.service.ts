import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPointNourrissage, getPointNourrissageIdentifier } from '../point-nourrissage.model';

export type EntityResponseType = HttpResponse<IPointNourrissage>;
export type EntityArrayResponseType = HttpResponse<IPointNourrissage[]>;

@Injectable({ providedIn: 'root' })
export class PointNourrissageService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/point-nourrissages');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pointNourrissage: IPointNourrissage): Observable<EntityResponseType> {
    return this.http.post<IPointNourrissage>(this.resourceUrl, pointNourrissage, { observe: 'response' });
  }

  update(pointNourrissage: IPointNourrissage): Observable<EntityResponseType> {
    return this.http.put<IPointNourrissage>(
      `${this.resourceUrl}/${getPointNourrissageIdentifier(pointNourrissage) as number}`,
      pointNourrissage,
      { observe: 'response' }
    );
  }

  partialUpdate(pointNourrissage: IPointNourrissage): Observable<EntityResponseType> {
    return this.http.patch<IPointNourrissage>(
      `${this.resourceUrl}/${getPointNourrissageIdentifier(pointNourrissage) as number}`,
      pointNourrissage,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPointNourrissage>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPointNourrissage[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPointNourrissageToCollectionIfMissing(
    pointNourrissageCollection: IPointNourrissage[],
    ...pointNourrissagesToCheck: (IPointNourrissage | null | undefined)[]
  ): IPointNourrissage[] {
    const pointNourrissages: IPointNourrissage[] = pointNourrissagesToCheck.filter(isPresent);
    if (pointNourrissages.length > 0) {
      const pointNourrissageCollectionIdentifiers = pointNourrissageCollection.map(
        pointNourrissageItem => getPointNourrissageIdentifier(pointNourrissageItem)!
      );
      const pointNourrissagesToAdd = pointNourrissages.filter(pointNourrissageItem => {
        const pointNourrissageIdentifier = getPointNourrissageIdentifier(pointNourrissageItem);
        if (pointNourrissageIdentifier == null || pointNourrissageCollectionIdentifiers.includes(pointNourrissageIdentifier)) {
          return false;
        }
        pointNourrissageCollectionIdentifiers.push(pointNourrissageIdentifier);
        return true;
      });
      return [...pointNourrissagesToAdd, ...pointNourrissageCollection];
    }
    return pointNourrissageCollection;
  }
}
