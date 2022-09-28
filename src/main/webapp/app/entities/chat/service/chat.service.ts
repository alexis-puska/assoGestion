import { HttpClient, HttpResponse, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import dayjs from 'dayjs/esm';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { Contrat } from 'app/entities/contrat/contrat.model';
import { getChatIdentifier, IChat } from '../chat.model';
import { VisiteVeterinaire } from 'app/entities/visite-veterinaire/visite-veterinaire.model';
import { FileUtilsService } from 'app/shared/util/file-utils.service';

export type EntityResponseType = HttpResponse<IChat>;
export type EntityArrayResponseType = HttpResponse<IChat[]>;

@Injectable({ providedIn: 'root' })
export class ChatService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/chats');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(chat: IChat, photo?: File): Observable<EntityResponseType> {
    const fd = new FormData();
    fd.append(
      'chat',
      new Blob([JSON.stringify(this.convertDateFromClient(chat))], {
        type: 'application/json',
      })
    );
    if (photo) {
      fd.append('photo', photo, FileUtilsService.encodeFileName(photo.name));
    }
    return this.http
      .post<IChat>(this.resourceUrl, fd, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(chat: IChat, photo?: File): Observable<EntityResponseType> {
    console.log(chat, photo);
    const fd = new FormData();
    fd.append(
      'chat',
      new Blob([JSON.stringify(this.convertDateFromClient(chat))], {
        type: 'application/json',
      })
    );
    if (photo) {
      fd.append('photo', photo, FileUtilsService.encodeFileName(photo.name));
    }
    return this.http
      .put<IChat>(`${this.resourceUrl}/${getChatIdentifier(chat) as number}`, fd, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(chat: IChat): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(chat);
    return this.http
      .patch<IChat>(`${this.resourceUrl}/${getChatIdentifier(chat) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IChat>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IChat[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addChatToCollectionIfMissing(chatCollection: IChat[], ...chatsToCheck: (IChat | null | undefined)[]): IChat[] {
    const chats: IChat[] = chatsToCheck.filter(isPresent);
    if (chats.length > 0) {
      const chatCollectionIdentifiers = chatCollection.map(chatItem => getChatIdentifier(chatItem)!);
      const chatsToAdd = chats.filter(chatItem => {
        const chatIdentifier = getChatIdentifier(chatItem);
        if (chatIdentifier == null || chatCollectionIdentifiers.includes(chatIdentifier)) {
          return false;
        }
        chatCollectionIdentifiers.push(chatIdentifier);
        return true;
      });
      return [...chatsToAdd, ...chatCollection];
    }
    return chatCollection;
  }

  protected convertDateFromClient(chat: IChat): IChat {
    const tmp = Object.assign({}, chat, {
      dateNaissance: chat.dateNaissance?.isValid() ? chat.dateNaissance.format(DATE_FORMAT) : undefined,
    });
    if (chat.contrat) {
      tmp.contrat = this.convertContrat(chat.contrat);
    }
    if (chat.visites) {
      const copyVisites: VisiteVeterinaire[] = [];
      chat.visites.forEach(v => {
        copyVisites.push(this.convertVisiteVeterinaireFromClient(v));
      });
      tmp.visites = copyVisites;
    }
    return tmp;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateNaissance = res.body.dateNaissance ? dayjs(res.body.dateNaissance) : undefined;
      if (res.body.contrat) {
        res.body.contrat.dateContrat = res.body.contrat.dateContrat ? dayjs(res.body.contrat.dateContrat) : undefined;
      }
      if (res.body.visites) {
        const visitesCopy: VisiteVeterinaire[] = [];
        res.body.visites.forEach(p => {
          visitesCopy.push(
            Object.assign({}, p, {
              dateVisite: p.dateVisite !== null ? dayjs(p.dateVisite) : undefined,
            })
          );
        });
        res.body.visites = visitesCopy;
      }
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((chat: IChat) => {
        chat.dateNaissance = chat.dateNaissance ? dayjs(chat.dateNaissance) : undefined;
        if (chat.contrat) {
          chat.contrat.dateContrat = chat.contrat.dateContrat ? dayjs(chat.contrat.dateContrat) : undefined;
        }
        if (chat.visites) {
          const visitesCopy: VisiteVeterinaire[] = [];
          chat.visites.forEach(p => {
            visitesCopy.push(
              Object.assign({}, p, {
                dateVisite: p.dateVisite !== null ? dayjs(p.dateVisite) : undefined,
              })
            );
          });
          chat.visites = visitesCopy;
        }
      });
    }
    return res;
  }

  private convertContrat(contrat: Contrat): Contrat {
    return Object.assign({}, contrat, {
      dateContrat: contrat.dateContrat?.isValid() ? contrat.dateContrat.format(DATE_FORMAT) : undefined,
    });
  }

  private convertVisiteVeterinaireFromClient(visiteVeterinaire: VisiteVeterinaire): VisiteVeterinaire {
    return Object.assign({}, visiteVeterinaire, {
      dateVisite: visiteVeterinaire.dateVisite?.isValid() ? visiteVeterinaire.dateVisite.format(DATE_FORMAT) : undefined,
    });
  }
}
