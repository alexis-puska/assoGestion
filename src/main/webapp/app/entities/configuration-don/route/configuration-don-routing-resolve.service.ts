import { HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Resolve, Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ConfigurationDon, IConfigurationDon } from '../configuration-don.model';
import { ConfigurationDonService } from '../service/configuration-don.service';

@Injectable({ providedIn: 'root' })
export class ConfigurationDonRoutingResolveService implements Resolve<IConfigurationDon> {
  constructor(protected service: ConfigurationDonService, protected router: Router) {}

  resolve(): Observable<IConfigurationDon> | Observable<never> {
    return this.service.getConfigurationDon().pipe(
      mergeMap((configurationDon: HttpResponse<ConfigurationDon>) => {
        if (configurationDon.body) {
          return of(configurationDon.body);
        } else {
          const configDon = new ConfigurationDon();
          configDon.id = 1;
          return of(configDon);
        }
      })
    );
  }
}
