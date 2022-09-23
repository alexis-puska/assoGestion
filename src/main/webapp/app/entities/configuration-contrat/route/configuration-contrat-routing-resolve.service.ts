import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IConfigurationContrat, ConfigurationContrat } from '../configuration-contrat.model';
import { ConfigurationContratService } from '../service/configuration-contrat.service';

@Injectable({ providedIn: 'root' })
export class ConfigurationContratRoutingResolveService implements Resolve<IConfigurationContrat> {
  constructor(protected service: ConfigurationContratService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IConfigurationContrat> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((configurationContrat: HttpResponse<ConfigurationContrat>) => {
          if (configurationContrat.body) {
            return of(configurationContrat.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ConfigurationContrat());
  }
}
