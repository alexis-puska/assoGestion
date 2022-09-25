import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRaceChat, getRaceChatIdentifier, RaceChat } from '../race-chat.model';
import { FamilleAccueil } from 'app/entities/famille-accueil/famille-accueil.model';

export type EntityResponseType = HttpResponse<IRaceChat>;
export type EntityArrayResponseType = HttpResponse<IRaceChat[]>;

@Injectable({ providedIn: 'root' })
export class RaceChatService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/race-chats');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(raceChat: IRaceChat): Observable<EntityResponseType> {
    return this.http.post<IRaceChat>(this.resourceUrl, raceChat, { observe: 'response' });
  }

  update(raceChat: IRaceChat): Observable<EntityResponseType> {
    return this.http.put<IRaceChat>(`${this.resourceUrl}/${getRaceChatIdentifier(raceChat) as number}`, raceChat, { observe: 'response' });
  }

  partialUpdate(raceChat: IRaceChat): Observable<EntityResponseType> {
    return this.http.patch<IRaceChat>(`${this.resourceUrl}/${getRaceChatIdentifier(raceChat) as number}`, raceChat, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRaceChat>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRaceChat[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRaceChatToCollectionIfMissing(raceChatCollection: IRaceChat[], ...raceChatsToCheck: (IRaceChat | null | undefined)[]): IRaceChat[] {
    const raceChats: IRaceChat[] = raceChatsToCheck.filter(isPresent);
    if (raceChats.length > 0) {
      const raceChatCollectionIdentifiers = raceChatCollection.map(raceChatItem => getRaceChatIdentifier(raceChatItem)!);
      const raceChatsToAdd = raceChats.filter(raceChatItem => {
        const raceChatIdentifier = getRaceChatIdentifier(raceChatItem);
        if (raceChatIdentifier == null || raceChatCollectionIdentifiers.includes(raceChatIdentifier)) {
          return false;
        }
        raceChatCollectionIdentifiers.push(raceChatIdentifier);
        return true;
      });
      return [...raceChatsToAdd, ...raceChatCollection];
    }
    return raceChatCollection;
  }

  findRaceChatAutocomplete(query: string): Observable<RaceChat[]> {
    let queryParam = new HttpParams();
    queryParam = queryParam.append('query', query);
    return this.http.get<RaceChat[]>(`${this.resourceUrl}/autocomplete`, {
      params: queryParam,
    });
  }
}
