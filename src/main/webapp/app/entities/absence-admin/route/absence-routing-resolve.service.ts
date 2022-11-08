import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAbsence, Absence } from '../../absence/absence.model';
import { AbsenceService } from '../../absence/service/absence.service';

@Injectable({ providedIn: 'root' })
export class AbsenceRoutingResolveService implements Resolve<IAbsence> {
  constructor(protected service: AbsenceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAbsence> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.findAdmin(id).pipe(
        mergeMap((absence: HttpResponse<Absence>) => {
          if (absence.body) {
            return of(absence.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Absence());
  }
}
