import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VisiteVeterinaireComponent } from './list/visite-veterinaire.component';
import { VisiteVeterinaireDetailComponent } from './detail/visite-veterinaire-detail.component';
import { VisiteVeterinaireUpdateComponent } from './update/visite-veterinaire-update.component';
import { VisiteVeterinaireDeleteDialogComponent } from './delete/visite-veterinaire-delete-dialog.component';
import { VisiteVeterinaireRoutingModule } from './route/visite-veterinaire-routing.module';
import { AdresseModule } from 'app/module/adresse/adresse.module';

@NgModule({
  imports: [SharedModule, VisiteVeterinaireRoutingModule, AdresseModule],
  declarations: [
    VisiteVeterinaireComponent,
    VisiteVeterinaireDetailComponent,
    VisiteVeterinaireUpdateComponent,
    VisiteVeterinaireDeleteDialogComponent,
  ],
  entryComponents: [VisiteVeterinaireDeleteDialogComponent],
})
export class VisiteVeterinaireModule {}
