import { NgModule } from '@angular/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { ActeVeterinaireModule } from 'app/module/acte-veterinaire/acte-veterinaire.module';
import { CommonsModule } from 'app/module/commons/commons.module';
import { SharedModule } from 'app/shared/shared.module';
import { VisiteVeterinaireDeleteDialogComponent } from './visite-veterinaire-delete-dialog.component';
import { VisiteVeterinaireListEditComponent } from './visite-veterinaire-list-edit.component';
import { VisiteVeterinaireUpdateDialogComponent } from './visite-veterinaire-update-dialog.component';

@NgModule({
  imports: [SharedModule, NgSelectModule, CommonsModule, ActeVeterinaireModule],
  declarations: [VisiteVeterinaireListEditComponent, VisiteVeterinaireUpdateDialogComponent, VisiteVeterinaireDeleteDialogComponent],
  exports: [VisiteVeterinaireListEditComponent],
  entryComponents: [VisiteVeterinaireUpdateDialogComponent, VisiteVeterinaireDeleteDialogComponent],
})
export class VisiteVeterinaireModule {}
