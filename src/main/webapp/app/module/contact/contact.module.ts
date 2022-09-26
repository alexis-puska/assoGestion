import { NgModule } from '@angular/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { SharedModule } from 'app/shared/shared.module';
import { ContactDeleteDialogComponent } from './contact-delete-dialog.component';
import { ContactListEditComponent } from './contact-list-edit.component';
import { ContactListComponent } from './contact-list.component';
import { ContactUpdateDialogComponent } from './contact-update-dialog.component';

@NgModule({
  imports: [SharedModule, NgSelectModule],
  declarations: [ContactListComponent, ContactListEditComponent, ContactUpdateDialogComponent, ContactDeleteDialogComponent],
  exports: [ContactListComponent, ContactListEditComponent],
  entryComponents: [ContactUpdateDialogComponent, ContactDeleteDialogComponent],
})
export class ContactModule {}
