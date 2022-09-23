import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IActeVeterinaire, ActeVeterinaire } from '../acte-veterinaire.model';
import { ActeVeterinaireService } from '../service/acte-veterinaire.service';

@Injectable({ providedIn: 'root' })
export class ActeVeterinaireRoutingResolveService implements Resolve<IActeVeterinaire> {
  constructor(protected service: ActeVeterinaireService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IActeVeterinaire> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((acteVeterinaire: HttpResponse<ActeVeterinaire>) => {
          if (acteVeterinaire.body) {
            return of(acteVeterinaire.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ActeVeterinaire());
  }
}
