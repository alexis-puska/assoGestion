import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRaceChat, RaceChat } from '../race-chat.model';
import { RaceChatService } from '../service/race-chat.service';

@Injectable({ providedIn: 'root' })
export class RaceChatRoutingResolveService implements Resolve<IRaceChat> {
  constructor(protected service: RaceChatService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRaceChat> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((raceChat: HttpResponse<RaceChat>) => {
          if (raceChat.body) {
            return of(raceChat.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RaceChat());
  }
}
