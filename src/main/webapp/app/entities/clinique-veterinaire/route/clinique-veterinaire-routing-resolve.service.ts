import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICliniqueVeterinaire, CliniqueVeterinaire } from '../clinique-veterinaire.model';
import { CliniqueVeterinaireService } from '../service/clinique-veterinaire.service';

@Injectable({ providedIn: 'root' })
export class CliniqueVeterinaireRoutingResolveService implements Resolve<ICliniqueVeterinaire> {
  constructor(protected service: CliniqueVeterinaireService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICliniqueVeterinaire> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cliniqueVeterinaire: HttpResponse<CliniqueVeterinaire>) => {
          if (cliniqueVeterinaire.body) {
            return of(cliniqueVeterinaire.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CliniqueVeterinaire());
  }
}
