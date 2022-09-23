import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVisiteVeterinaire, getVisiteVeterinaireIdentifier } from '../visite-veterinaire.model';

export type EntityResponseType = HttpResponse<IVisiteVeterinaire>;
export type EntityArrayResponseType = HttpResponse<IVisiteVeterinaire[]>;

@Injectable({ providedIn: 'root' })
export class VisiteVeterinaireService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/visite-veterinaires');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(visiteVeterinaire: IVisiteVeterinaire): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(visiteVeterinaire);
    return this.http
      .post<IVisiteVeterinaire>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(visiteVeterinaire: IVisiteVeterinaire): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(visiteVeterinaire);
    return this.http
      .put<IVisiteVeterinaire>(`${this.resourceUrl}/${getVisiteVeterinaireIdentifier(visiteVeterinaire) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(visiteVeterinaire: IVisiteVeterinaire): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(visiteVeterinaire);
    return this.http
      .patch<IVisiteVeterinaire>(`${this.resourceUrl}/${getVisiteVeterinaireIdentifier(visiteVeterinaire) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IVisiteVeterinaire>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVisiteVeterinaire[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addVisiteVeterinaireToCollectionIfMissing(
    visiteVeterinaireCollection: IVisiteVeterinaire[],
    ...visiteVeterinairesToCheck: (IVisiteVeterinaire | null | undefined)[]
  ): IVisiteVeterinaire[] {
    const visiteVeterinaires: IVisiteVeterinaire[] = visiteVeterinairesToCheck.filter(isPresent);
    if (visiteVeterinaires.length > 0) {
      const visiteVeterinaireCollectionIdentifiers = visiteVeterinaireCollection.map(
        visiteVeterinaireItem => getVisiteVeterinaireIdentifier(visiteVeterinaireItem)!
      );
      const visiteVeterinairesToAdd = visiteVeterinaires.filter(visiteVeterinaireItem => {
        const visiteVeterinaireIdentifier = getVisiteVeterinaireIdentifier(visiteVeterinaireItem);
        if (visiteVeterinaireIdentifier == null || visiteVeterinaireCollectionIdentifiers.includes(visiteVeterinaireIdentifier)) {
          return false;
        }
        visiteVeterinaireCollectionIdentifiers.push(visiteVeterinaireIdentifier);
        return true;
      });
      return [...visiteVeterinairesToAdd, ...visiteVeterinaireCollection];
    }
    return visiteVeterinaireCollection;
  }

  protected convertDateFromClient(visiteVeterinaire: IVisiteVeterinaire): IVisiteVeterinaire {
    return Object.assign({}, visiteVeterinaire, {
      dateVisite: visiteVeterinaire.dateVisite?.isValid() ? visiteVeterinaire.dateVisite.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateVisite = res.body.dateVisite ? dayjs(res.body.dateVisite) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((visiteVeterinaire: IVisiteVeterinaire) => {
        visiteVeterinaire.dateVisite = visiteVeterinaire.dateVisite ? dayjs(visiteVeterinaire.dateVisite) : undefined;
      });
    }
    return res;
  }
}
