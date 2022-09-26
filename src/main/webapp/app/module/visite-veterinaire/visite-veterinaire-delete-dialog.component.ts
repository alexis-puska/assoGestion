import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IVisiteVeterinaire } from 'app/entities/visite-veterinaire/visite-veterinaire.model';

@Component({
  templateUrl: './visite-veterinaire-delete-dialog.component.html',
})
export class VisiteVeterinaireDeleteDialogComponent {
  visiteVeterinaire?: IVisiteVeterinaire;

  constructor(protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(): void {
    this.activeModal.close();
  }
}
