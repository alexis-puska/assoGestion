import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PointCaptureComponent } from '../list/point-capture.component';
import { PointCaptureDetailComponent } from '../detail/point-capture-detail.component';
import { PointCaptureUpdateComponent } from '../update/point-capture-update.component';
import { PointCaptureRoutingResolveService } from './point-capture-routing-resolve.service';

const pointCaptureRoute: Routes = [
  {
    path: '',
    component: PointCaptureComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PointCaptureDetailComponent,
    resolve: {
      pointCapture: PointCaptureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PointCaptureUpdateComponent,
    resolve: {
      pointCapture: PointCaptureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PointCaptureUpdateComponent,
    resolve: {
      pointCapture: PointCaptureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pointCaptureRoute)],
  exports: [RouterModule],
})
export class PointCaptureRoutingModule {}
