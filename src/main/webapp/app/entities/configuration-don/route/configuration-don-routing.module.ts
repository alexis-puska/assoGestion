import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ConfigurationDonUpdateComponent } from '../update/configuration-don-update.component';
import { ConfigurationDonRoutingResolveService } from './configuration-don-routing-resolve.service';

const configurationDonRoute: Routes = [
  {
    path: '',
    component: ConfigurationDonUpdateComponent,
    resolve: {
      configurationDon: ConfigurationDonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(configurationDonRoute)],
  exports: [RouterModule],
})
export class ConfigurationDonRoutingModule {}
