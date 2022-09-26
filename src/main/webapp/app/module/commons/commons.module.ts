import { NgModule } from '@angular/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { SharedModule } from 'app/shared/shared.module';
import { AdresseEdit2Component } from './adresse-edit2.component';
import { AdresseViewComponent } from './adresse-view.component';
import { ContactDeleteDialogComponent } from './contact-delete-dialog.component';
import { ContactListEditComponent } from './contact-list-edit.component';
import { ContactListComponent } from './contact-list.component';
import { ContactUpdateDialogComponent } from './contact-update-dialog.component';
import { FamilleAccueilAutocompleteControlComponent } from './famille-acceuil-autocomplete-control.component';
import { PointCaptureAutocompleteControlComponent } from './point-capture-autocomplete-control.component';
import { RaceChatAutocompleteControlComponent } from './race-chat-autocomplete-control.component';

@NgModule({
  imports: [SharedModule, NgSelectModule],
  declarations: [
    AdresseViewComponent,
    ContactListComponent,
    ContactListEditComponent,
    ContactUpdateDialogComponent,
    ContactDeleteDialogComponent,
    FamilleAccueilAutocompleteControlComponent,
    RaceChatAutocompleteControlComponent,
    PointCaptureAutocompleteControlComponent,
    AdresseEdit2Component,
  ],
  exports: [
    AdresseViewComponent,
    ContactListComponent,
    ContactListEditComponent,
    FamilleAccueilAutocompleteControlComponent,
    RaceChatAutocompleteControlComponent,
    PointCaptureAutocompleteControlComponent,
    AdresseEdit2Component,
  ],
  entryComponents: [ContactUpdateDialogComponent, ContactDeleteDialogComponent],
})
export class CommonsModule {}
