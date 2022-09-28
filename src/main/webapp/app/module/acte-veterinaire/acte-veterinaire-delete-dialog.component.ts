import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IActeVeterinaire } from 'app/entities/acte-veterinaire/acte-veterinaire.model';

@Component({
  templateUrl: './acte-veterinaire-delete-dialog.component.html',
})
export class ActeVeterinaireDeleteDialogComponent {
  acteVeterinaire?: IActeVeterinaire;

  constructor(protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(): void {
    this.activeModal.close();
  }
}
