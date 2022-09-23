import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICliniqueVeterinaire } from '../clinique-veterinaire.model';
import { CliniqueVeterinaireService } from '../service/clinique-veterinaire.service';

@Component({
  templateUrl: './clinique-veterinaire-delete-dialog.component.html',
})
export class CliniqueVeterinaireDeleteDialogComponent {
  cliniqueVeterinaire?: ICliniqueVeterinaire;

  constructor(protected cliniqueVeterinaireService: CliniqueVeterinaireService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cliniqueVeterinaireService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
