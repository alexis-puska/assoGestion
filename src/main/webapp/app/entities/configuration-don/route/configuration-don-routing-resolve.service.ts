import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IConfigurationDon, ConfigurationDon } from '../configuration-don.model';
import { ConfigurationDonService } from '../service/configuration-don.service';

@Injectable({ providedIn: 'root' })
export class ConfigurationDonRoutingResolveService implements Resolve<IConfigurationDon> {
  constructor(protected service: ConfigurationDonService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IConfigurationDon> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((configurationDon: HttpResponse<ConfigurationDon>) => {
          if (configurationDon.body) {
            return of(configurationDon.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ConfigurationDon());
  }
}
