import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFamilleAccueil, FamilleAccueil } from '../famille-accueil.model';
import { FamilleAccueilService } from '../service/famille-accueil.service';

@Injectable({ providedIn: 'root' })
export class FamilleAccueilRoutingResolveService implements Resolve<IFamilleAccueil> {
  constructor(protected service: FamilleAccueilService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFamilleAccueil> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((familleAccueil: HttpResponse<FamilleAccueil>) => {
          if (familleAccueil.body) {
            return of(familleAccueil.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FamilleAccueil());
  }
}
