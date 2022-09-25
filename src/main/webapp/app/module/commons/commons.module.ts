import { NgModule } from '@angular/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { SharedModule } from 'app/shared/shared.module';
import { AdresseEditComponent } from './adresse-edit.component';
import { AdresseViewComponent } from './adresse-view.component';
import { ContactDeleteDialogComponent } from './contact-delete-dialog.component';
import { ContactListEditComponent } from './contact-list-edit.component';
import { ContactListComponent } from './contact-list.component';
import { ContactUpdateDialogComponent } from './contact-update-dialog.component';
import { FamilleAccueilAutocompleteControlComponent } from './famille-acceuil-autocomplete-control.component';

@NgModule({
  imports: [SharedModule, NgSelectModule],
  declarations: [
    AdresseEditComponent,
    AdresseViewComponent,
    ContactListComponent,
    ContactListEditComponent,
    ContactUpdateDialogComponent,
    ContactDeleteDialogComponent,
    FamilleAccueilAutocompleteControlComponent,
  ],
  exports: [
    AdresseEditComponent,
    AdresseViewComponent,
    ContactListComponent,
    ContactListEditComponent,
    FamilleAccueilAutocompleteControlComponent,
  ],
  entryComponents: [ContactUpdateDialogComponent, ContactDeleteDialogComponent],
})
export class CommonsModule {}
