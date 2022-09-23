import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ActeVeterinaireComponent } from '../list/acte-veterinaire.component';
import { ActeVeterinaireDetailComponent } from '../detail/acte-veterinaire-detail.component';
import { ActeVeterinaireUpdateComponent } from '../update/acte-veterinaire-update.component';
import { ActeVeterinaireRoutingResolveService } from './acte-veterinaire-routing-resolve.service';

const acteVeterinaireRoute: Routes = [
  {
    path: '',
    component: ActeVeterinaireComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ActeVeterinaireDetailComponent,
    resolve: {
      acteVeterinaire: ActeVeterinaireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ActeVeterinaireUpdateComponent,
    resolve: {
      acteVeterinaire: ActeVeterinaireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ActeVeterinaireUpdateComponent,
    resolve: {
      acteVeterinaire: ActeVeterinaireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(acteVeterinaireRoute)],
  exports: [RouterModule],
})
export class ActeVeterinaireRoutingModule {}
