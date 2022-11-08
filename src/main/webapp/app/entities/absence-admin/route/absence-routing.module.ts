import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AbsenceComponent } from '../list/absence.component';
import { AbsenceDetailComponent } from '../detail/absence-detail.component';
import { AbsenceUpdateComponent } from '../update/absence-update.component';
import { AbsenceRoutingResolveService } from './absence-routing-resolve.service';

const absenceRoute: Routes = [
  {
    path: '',
    component: AbsenceComponent,
    data: {
      authorities: ['ROLE_ADMIN'],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AbsenceDetailComponent,
    resolve: {
      absence: AbsenceRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_ADMIN'],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AbsenceUpdateComponent,
    resolve: {
      absence: AbsenceRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_ADMIN'],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AbsenceUpdateComponent,
    resolve: {
      absence: AbsenceRoutingResolveService,
    },
    data: {
      authorities: ['ROLE_ADMIN'],
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(absenceRoute)],
  exports: [RouterModule],
})
export class AbsenceRoutingModule {}
