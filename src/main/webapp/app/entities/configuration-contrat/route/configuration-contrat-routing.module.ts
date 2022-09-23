import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ConfigurationContratComponent } from '../list/configuration-contrat.component';
import { ConfigurationContratDetailComponent } from '../detail/configuration-contrat-detail.component';
import { ConfigurationContratUpdateComponent } from '../update/configuration-contrat-update.component';
import { ConfigurationContratRoutingResolveService } from './configuration-contrat-routing-resolve.service';

const configurationContratRoute: Routes = [
  {
    path: '',
    component: ConfigurationContratComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ConfigurationContratDetailComponent,
    resolve: {
      configurationContrat: ConfigurationContratRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ConfigurationContratUpdateComponent,
    resolve: {
      configurationContrat: ConfigurationContratRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ConfigurationContratUpdateComponent,
    resolve: {
      configurationContrat: ConfigurationContratRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(configurationContratRoute)],
  exports: [RouterModule],
})
export class ConfigurationContratRoutingModule {}
