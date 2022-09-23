import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFamilleAccueil } from '../famille-accueil.model';
import { FamilleAccueilService } from '../service/famille-accueil.service';

@Component({
  templateUrl: './famille-accueil-delete-dialog.component.html',
})
export class FamilleAccueilDeleteDialogComponent {
  familleAccueil?: IFamilleAccueil;

  constructor(protected familleAccueilService: FamilleAccueilService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.familleAccueilService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
