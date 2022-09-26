import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Contact, IContact } from 'app/entities/contact/contact.model';

@Component({
  selector: 'jhi-contact-update-dialog',
  templateUrl: './contact-update-dialog.component.html',
})
export class ContactUpdateDialogComponent implements OnInit {
  isSaving = false;

  // needed to open modal
  contact: Contact | null = null;

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required]],
    prenom: [null, [Validators.required]],
    mail: [],
    telMobile: [],
    telFixe: [],
  });

  constructor(protected fb: FormBuilder, public activeModal: NgbActiveModal) {}

  ngOnInit(): void {
    if (this.contact) {
      this.updateForm(this.contact);
    }
  }

  save(): void {
    this.isSaving = true;
    this.activeModal.close(this.createFromForm());
  }

  clean(): void {
    this.activeModal.dismiss();
  }

  protected updateForm(contact: IContact): void {
    this.editForm.patchValue({
      id: contact.id,
      nom: contact.nom,
      prenom: contact.prenom,
      mail: contact.mail,
      telMobile: contact.telMobile,
      telFixe: contact.telFixe,
    });
  }

  protected createFromForm(): IContact {
    return {
      ...new Contact(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      mail: this.editForm.get(['mail'])!.value,
      telMobile: this.editForm.get(['telMobile'])!.value,
      telFixe: this.editForm.get(['telFixe'])!.value,
    };
  }
}
