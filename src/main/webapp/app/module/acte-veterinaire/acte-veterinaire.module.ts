import { NgModule } from '@angular/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { SharedModule } from 'app/shared/shared.module';
import { ActeVeterinaireDeleteDialogComponent } from './acte-veterinaire-delete-dialog.component';
import { ActeVeterinaireListEditComponent } from './acte-veterinaire-list-edit.component';
import { ActeVeterinaireUpdateDialogComponent } from './acte-veterinaire-update-dialog.component';
import { CommonsModule } from 'app/module/commons/commons.module';

@NgModule({
  imports: [SharedModule, NgSelectModule, CommonsModule],
  declarations: [ActeVeterinaireListEditComponent, ActeVeterinaireUpdateDialogComponent, ActeVeterinaireDeleteDialogComponent],
  exports: [ActeVeterinaireListEditComponent],
  entryComponents: [ActeVeterinaireUpdateDialogComponent, ActeVeterinaireDeleteDialogComponent],
})
export class ActeVeterinaireModule {}
