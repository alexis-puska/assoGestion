import { HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Resolve, Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ConfigurationAsso, IConfigurationAsso } from '../configuration-asso.model';
import { ConfigurationAssoService } from '../service/configuration-asso.service';

@Injectable({ providedIn: 'root' })
export class ConfigurationAssoRoutingResolveService implements Resolve<IConfigurationAsso> {
  constructor(protected service: ConfigurationAssoService, protected router: Router) {}

  resolve(): Observable<IConfigurationAsso> | Observable<never> {
    return this.service.getConfigurationAsso().pipe(
      mergeMap((configurationAsso: HttpResponse<ConfigurationAsso>) => {
        if (configurationAsso.body) {
          return of(configurationAsso.body);
        } else {
          const configDon = new ConfigurationAsso();
          configDon.id = 1;
          return of(configDon);
        }
      })
    );
  }
}
