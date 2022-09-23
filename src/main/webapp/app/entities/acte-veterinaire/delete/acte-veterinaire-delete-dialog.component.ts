import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IActeVeterinaire } from '../acte-veterinaire.model';
import { ActeVeterinaireService } from '../service/acte-veterinaire.service';

@Component({
  templateUrl: './acte-veterinaire-delete-dialog.component.html',
})
export class ActeVeterinaireDeleteDialogComponent {
  acteVeterinaire?: IActeVeterinaire;

  constructor(protected acteVeterinaireService: ActeVeterinaireService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.acteVeterinaireService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
