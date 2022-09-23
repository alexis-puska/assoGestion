import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FamilleAccueilComponent } from '../list/famille-accueil.component';
import { FamilleAccueilDetailComponent } from '../detail/famille-accueil-detail.component';
import { FamilleAccueilUpdateComponent } from '../update/famille-accueil-update.component';
import { FamilleAccueilRoutingResolveService } from './famille-accueil-routing-resolve.service';

const familleAccueilRoute: Routes = [
  {
    path: '',
    component: FamilleAccueilComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FamilleAccueilDetailComponent,
    resolve: {
      familleAccueil: FamilleAccueilRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FamilleAccueilUpdateComponent,
    resolve: {
      familleAccueil: FamilleAccueilRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FamilleAccueilUpdateComponent,
    resolve: {
      familleAccueil: FamilleAccueilRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(familleAccueilRoute)],
  exports: [RouterModule],
})
export class FamilleAccueilRoutingModule {}
