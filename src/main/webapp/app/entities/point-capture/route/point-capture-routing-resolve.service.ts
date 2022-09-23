import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPointCapture, PointCapture } from '../point-capture.model';
import { PointCaptureService } from '../service/point-capture.service';

@Injectable({ providedIn: 'root' })
export class PointCaptureRoutingResolveService implements Resolve<IPointCapture> {
  constructor(protected service: PointCaptureService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPointCapture> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pointCapture: HttpResponse<PointCapture>) => {
          if (pointCapture.body) {
            return of(pointCapture.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PointCapture());
  }
}
