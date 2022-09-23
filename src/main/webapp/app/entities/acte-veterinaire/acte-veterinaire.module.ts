import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ActeVeterinaireComponent } from './list/acte-veterinaire.component';
import { ActeVeterinaireDetailComponent } from './detail/acte-veterinaire-detail.component';
import { ActeVeterinaireUpdateComponent } from './update/acte-veterinaire-update.component';
import { ActeVeterinaireDeleteDialogComponent } from './delete/acte-veterinaire-delete-dialog.component';
import { ActeVeterinaireRoutingModule } from './route/acte-veterinaire-routing.module';

@NgModule({
  imports: [SharedModule, ActeVeterinaireRoutingModule],
  declarations: [
    ActeVeterinaireComponent,
    ActeVeterinaireDetailComponent,
    ActeVeterinaireUpdateComponent,
    ActeVeterinaireDeleteDialogComponent,
  ],
  entryComponents: [ActeVeterinaireDeleteDialogComponent],
})
export class ActeVeterinaireModule {}
