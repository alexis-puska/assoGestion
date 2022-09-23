import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVisiteVeterinaire } from '../visite-veterinaire.model';
import { VisiteVeterinaireService } from '../service/visite-veterinaire.service';

@Component({
  templateUrl: './visite-veterinaire-delete-dialog.component.html',
})
export class VisiteVeterinaireDeleteDialogComponent {
  visiteVeterinaire?: IVisiteVeterinaire;

  constructor(protected visiteVeterinaireService: VisiteVeterinaireService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.visiteVeterinaireService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
