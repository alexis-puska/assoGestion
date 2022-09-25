import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AdresseEditComponent } from './adresse-edit.component';
import { AdresseViewComponent } from './adresse-view.component';
import { ContactDeleteDialogComponent } from './contact-delete-dialog.component';
import { ContactListEditComponent } from './contact-list-edit.component';
import { ContactListComponent } from './contact-list.component';
import { ContactUpdateDialogComponent } from './contact-update-dialog.component';

@NgModule({
  imports: [SharedModule],
  declarations: [
    AdresseEditComponent,
    AdresseViewComponent,
    ContactListComponent,
    ContactListEditComponent,
    ContactUpdateDialogComponent,
    ContactDeleteDialogComponent,
  ],
  exports: [AdresseEditComponent, AdresseViewComponent, ContactListComponent, ContactListEditComponent],
  entryComponents: [ContactUpdateDialogComponent, ContactDeleteDialogComponent],
})
export class CommonsModule {}
