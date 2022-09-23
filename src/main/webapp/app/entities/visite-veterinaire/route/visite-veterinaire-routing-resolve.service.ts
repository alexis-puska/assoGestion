import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVisiteVeterinaire, VisiteVeterinaire } from '../visite-veterinaire.model';
import { VisiteVeterinaireService } from '../service/visite-veterinaire.service';

@Injectable({ providedIn: 'root' })
export class VisiteVeterinaireRoutingResolveService implements Resolve<IVisiteVeterinaire> {
  constructor(protected service: VisiteVeterinaireService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVisiteVeterinaire> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((visiteVeterinaire: HttpResponse<VisiteVeterinaire>) => {
          if (visiteVeterinaire.body) {
            return of(visiteVeterinaire.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new VisiteVeterinaire());
  }
}
