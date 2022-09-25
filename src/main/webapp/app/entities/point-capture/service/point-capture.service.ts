import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPointCapture, getPointCaptureIdentifier, PointCapture } from '../point-capture.model';

export type EntityResponseType = HttpResponse<IPointCapture>;
export type EntityArrayResponseType = HttpResponse<IPointCapture[]>;

@Injectable({ providedIn: 'root' })
export class PointCaptureService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/point-captures');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pointCapture: IPointCapture): Observable<EntityResponseType> {
    return this.http.post<IPointCapture>(this.resourceUrl, pointCapture, { observe: 'response' });
  }

  update(pointCapture: IPointCapture): Observable<EntityResponseType> {
    return this.http.put<IPointCapture>(`${this.resourceUrl}/${getPointCaptureIdentifier(pointCapture) as number}`, pointCapture, {
      observe: 'response',
    });
  }

  partialUpdate(pointCapture: IPointCapture): Observable<EntityResponseType> {
    return this.http.patch<IPointCapture>(`${this.resourceUrl}/${getPointCaptureIdentifier(pointCapture) as number}`, pointCapture, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPointCapture>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPointCapture[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPointCaptureToCollectionIfMissing(
    pointCaptureCollection: IPointCapture[],
    ...pointCapturesToCheck: (IPointCapture | null | undefined)[]
  ): IPointCapture[] {
    const pointCaptures: IPointCapture[] = pointCapturesToCheck.filter(isPresent);
    if (pointCaptures.length > 0) {
      const pointCaptureCollectionIdentifiers = pointCaptureCollection.map(
        pointCaptureItem => getPointCaptureIdentifier(pointCaptureItem)!
      );
      const pointCapturesToAdd = pointCaptures.filter(pointCaptureItem => {
        const pointCaptureIdentifier = getPointCaptureIdentifier(pointCaptureItem);
        if (pointCaptureIdentifier == null || pointCaptureCollectionIdentifiers.includes(pointCaptureIdentifier)) {
          return false;
        }
        pointCaptureCollectionIdentifiers.push(pointCaptureIdentifier);
        return true;
      });
      return [...pointCapturesToAdd, ...pointCaptureCollection];
    }
    return pointCaptureCollection;
  }

  findPointCaptureAutocomplete(query: string): Observable<PointCapture[]> {
    let queryParam = new HttpParams();
    queryParam = queryParam.append('query', query);
    return this.http.get<PointCapture[]>(`${this.resourceUrl}/autocomplete`, {
      params: queryParam,
    });
  }
}
