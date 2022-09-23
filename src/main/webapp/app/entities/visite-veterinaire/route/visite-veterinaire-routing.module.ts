import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VisiteVeterinaireComponent } from '../list/visite-veterinaire.component';
import { VisiteVeterinaireDetailComponent } from '../detail/visite-veterinaire-detail.component';
import { VisiteVeterinaireUpdateComponent } from '../update/visite-veterinaire-update.component';
import { VisiteVeterinaireRoutingResolveService } from './visite-veterinaire-routing-resolve.service';

const visiteVeterinaireRoute: Routes = [
  {
    path: '',
    component: VisiteVeterinaireComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VisiteVeterinaireDetailComponent,
    resolve: {
      visiteVeterinaire: VisiteVeterinaireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VisiteVeterinaireUpdateComponent,
    resolve: {
      visiteVeterinaire: VisiteVeterinaireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VisiteVeterinaireUpdateComponent,
    resolve: {
      visiteVeterinaire: VisiteVeterinaireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(visiteVeterinaireRoute)],
  exports: [RouterModule],
})
export class VisiteVeterinaireRoutingModule {}
