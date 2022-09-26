import { Component, Input } from '@angular/core';
import { Contact, IContact } from 'app/entities/contact/contact.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { VisiteVeterinaireUpdateDialogComponent } from './visite-veterinaire-update-dialog.component';
import { VisiteVeterinaireDeleteDialogComponent } from './visite-veterinaire-delete-dialog.component';

@Component({
  selector: 'jhi-visite-veterinaire-list-edit',
  templateUrl: './visite-veterinaire-list-edit.component.html',
})
export class VisiteVeterinaireListEditComponent {
  @Input()
  contacts: IContact[] | null = [];

  constructor(private modalService: NgbModal) {}

  createContact(): void {
    this.openModal();
  }

  updateContact(contact: Contact): void {
    this.openModal(contact);
  }

  deleteContact(contact: Contact): void {
    const ngbModal = this.modalService.open(VisiteVeterinaireDeleteDialogComponent as Component, {
      size: 'lg',
      backdrop: 'static',
    });

    ngbModal.componentInstance.contact = contact;
    ngbModal.result.then(
      () => {
        if (this.contacts) {
          const index = this.contacts.indexOf(contact, 0);
          if (index > -1) {
            this.contacts.splice(index, 1);
          }
        }
      },
      () => {}
    );
  }

  private openModal(contact?: Contact): void {
    if (this.contacts) {
      let isNew = false;
      let con: Contact;
      let index: number;
      if (!contact) {
        isNew = true;
        con = new Contact();
      } else {
        con = Object.assign({}, contact);
        index = this.contacts.indexOf(contact);
      }
      const ngbModal = this.modalService.open(VisiteVeterinaireUpdateDialogComponent as Component, {
        size: 'lg',
        backdrop: 'static',
      });
      ngbModal.componentInstance.contact = con;
      ngbModal.result.then(
        c => {
          if (this.contacts) {
            if (isNew) {
              this.contacts.push(c);
            } else {
              this.contacts[index] = c;
            }
          }
        },
        () => {}
      );
    }
  }
}
