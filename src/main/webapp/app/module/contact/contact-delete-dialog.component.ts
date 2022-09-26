import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IContact } from 'app/entities/contact/contact.model';

@Component({
  templateUrl: './contact-delete-dialog.component.html',
})
export class ContactDeleteDialogComponent {
  contact?: IContact;

  constructor(protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(): void {
    this.activeModal.close();
  }
}
