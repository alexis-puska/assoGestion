import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DonateurComponent } from '../list/donateur.component';
import { DonateurDetailComponent } from '../detail/donateur-detail.component';
import { DonateurUpdateComponent } from '../update/donateur-update.component';
import { DonateurRoutingResolveService } from './donateur-routing-resolve.service';

const donateurRoute: Routes = [
  {
    path: '',
    component: DonateurComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DonateurDetailComponent,
    resolve: {
      donateur: DonateurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DonateurUpdateComponent,
    resolve: {
      donateur: DonateurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DonateurUpdateComponent,
    resolve: {
      donateur: DonateurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(donateurRoute)],
  exports: [RouterModule],
})
export class DonateurRoutingModule {}
