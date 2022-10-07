import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
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
    return this.http
      .post<IDonateur>(this.resourceUrl, this.convertDateFromClient(donateur), { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(donateur: IDonateur): Observable<EntityResponseType> {
    return this.http
      .put<IDonateur>(`${this.resourceUrl}/${getDonateurIdentifier(donateur) as number}`, this.convertDateFromClient(donateur), {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(donateur: IDonateur): Observable<EntityResponseType> {
    return this.http
      .patch<IDonateur>(`${this.resourceUrl}/${getDonateurIdentifier(donateur) as number}`, this.convertDateFromClient(donateur), {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDonateur>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDonateur[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  fillCerfa(id: number): Observable<HttpResponse<Blob>> {
    return this.http.get<Blob>(`${this.resourceUrl}/cerfa/${id}`, { responseType: 'blob' as 'json', observe: 'response' });
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

  protected convertDateFromClient(donateur: IDonateur): IDonateur {
    return Object.assign({}, donateur, {
      dateDon: donateur.dateDon?.isValid() ? donateur.dateDon.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateDon = res.body.dateDon ? dayjs(res.body.dateDon) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((donateur: IDonateur) => {
        donateur.dateDon = donateur.dateDon ? dayjs(donateur.dateDon) : undefined;
      });
    }
    return res;
  }
}
