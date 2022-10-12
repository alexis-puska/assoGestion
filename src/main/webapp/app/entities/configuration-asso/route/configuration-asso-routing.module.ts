import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ConfigurationAssoUpdateComponent } from '../update/configuration-asso-update.component';
import { ConfigurationAssoRoutingResolveService } from './configuration-asso-routing-resolve.service';

const configurationAssoRoute: Routes = [
  {
    path: '',
    component: ConfigurationAssoUpdateComponent,
    resolve: {
      configurationAsso: ConfigurationAssoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(configurationAssoRoute)],
  exports: [RouterModule],
})
export class ConfigurationAssoRoutingModule {}
