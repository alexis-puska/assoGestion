import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ConfigurationDonComponent } from '../list/configuration-don.component';
import { ConfigurationDonDetailComponent } from '../detail/configuration-don-detail.component';
import { ConfigurationDonUpdateComponent } from '../update/configuration-don-update.component';
import { ConfigurationDonRoutingResolveService } from './configuration-don-routing-resolve.service';

const configurationDonRoute: Routes = [
  {
    path: '',
    component: ConfigurationDonComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ConfigurationDonDetailComponent,
    resolve: {
      configurationDon: ConfigurationDonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ConfigurationDonUpdateComponent,
    resolve: {
      configurationDon: ConfigurationDonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
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
