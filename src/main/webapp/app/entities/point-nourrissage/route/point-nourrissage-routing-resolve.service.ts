import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPointNourrissage, PointNourrissage } from '../point-nourrissage.model';
import { PointNourrissageService } from '../service/point-nourrissage.service';

@Injectable({ providedIn: 'root' })
export class PointNourrissageRoutingResolveService implements Resolve<IPointNourrissage> {
  constructor(protected service: PointNourrissageService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPointNourrissage> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pointNourrissage: HttpResponse<PointNourrissage>) => {
          if (pointNourrissage.body) {
            return of(pointNourrissage.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PointNourrissage());
  }
}
