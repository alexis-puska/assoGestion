import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CliniqueVeterinaireComponent } from './list/clinique-veterinaire.component';
import { CliniqueVeterinaireDetailComponent } from './detail/clinique-veterinaire-detail.component';
import { CliniqueVeterinaireUpdateComponent } from './update/clinique-veterinaire-update.component';
import { CliniqueVeterinaireDeleteDialogComponent } from './delete/clinique-veterinaire-delete-dialog.component';
import { CliniqueVeterinaireRoutingModule } from './route/clinique-veterinaire-routing.module';
import { CommonsModule } from 'app/module/commons/commons.module';

@NgModule({
  imports: [SharedModule, CliniqueVeterinaireRoutingModule, CommonsModule],
  declarations: [
    CliniqueVeterinaireComponent,
    CliniqueVeterinaireDetailComponent,
    CliniqueVeterinaireUpdateComponent,
    CliniqueVeterinaireDeleteDialogComponent,
  ],
  entryComponents: [CliniqueVeterinaireDeleteDialogComponent],
})
export class CliniqueVeterinaireModule {}
