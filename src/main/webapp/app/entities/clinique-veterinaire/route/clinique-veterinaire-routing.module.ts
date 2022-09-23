import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CliniqueVeterinaireComponent } from '../list/clinique-veterinaire.component';
import { CliniqueVeterinaireDetailComponent } from '../detail/clinique-veterinaire-detail.component';
import { CliniqueVeterinaireUpdateComponent } from '../update/clinique-veterinaire-update.component';
import { CliniqueVeterinaireRoutingResolveService } from './clinique-veterinaire-routing-resolve.service';

const cliniqueVeterinaireRoute: Routes = [
  {
    path: '',
    component: CliniqueVeterinaireComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CliniqueVeterinaireDetailComponent,
    resolve: {
      cliniqueVeterinaire: CliniqueVeterinaireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CliniqueVeterinaireUpdateComponent,
    resolve: {
      cliniqueVeterinaire: CliniqueVeterinaireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CliniqueVeterinaireUpdateComponent,
    resolve: {
      cliniqueVeterinaire: CliniqueVeterinaireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cliniqueVeterinaireRoute)],
  exports: [RouterModule],
})
export class CliniqueVeterinaireRoutingModule {}
