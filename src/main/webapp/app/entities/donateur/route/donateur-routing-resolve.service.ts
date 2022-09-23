import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDonateur, Donateur } from '../donateur.model';
import { DonateurService } from '../service/donateur.service';

@Injectable({ providedIn: 'root' })
export class DonateurRoutingResolveService implements Resolve<IDonateur> {
  constructor(protected service: DonateurService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDonateur> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((donateur: HttpResponse<Donateur>) => {
          if (donateur.body) {
            return of(donateur.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Donateur());
  }
}
