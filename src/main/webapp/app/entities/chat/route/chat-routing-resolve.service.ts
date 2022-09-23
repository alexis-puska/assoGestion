import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IChat, Chat } from '../chat.model';
import { ChatService } from '../service/chat.service';

@Injectable({ providedIn: 'root' })
export class ChatRoutingResolveService implements Resolve<IChat> {
  constructor(protected service: ChatService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IChat> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((chat: HttpResponse<Chat>) => {
          if (chat.body) {
            return of(chat.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Chat());
  }
}
