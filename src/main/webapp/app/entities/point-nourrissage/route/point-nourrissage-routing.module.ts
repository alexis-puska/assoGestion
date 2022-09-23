import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PointNourrissageComponent } from '../list/point-nourrissage.component';
import { PointNourrissageDetailComponent } from '../detail/point-nourrissage-detail.component';
import { PointNourrissageUpdateComponent } from '../update/point-nourrissage-update.component';
import { PointNourrissageRoutingResolveService } from './point-nourrissage-routing-resolve.service';

const pointNourrissageRoute: Routes = [
  {
    path: '',
    component: PointNourrissageComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PointNourrissageDetailComponent,
    resolve: {
      pointNourrissage: PointNourrissageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PointNourrissageUpdateComponent,
    resolve: {
      pointNourrissage: PointNourrissageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PointNourrissageUpdateComponent,
    resolve: {
      pointNourrissage: PointNourrissageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pointNourrissageRoute)],
  exports: [RouterModule],
})
export class PointNourrissageRoutingModule {}
