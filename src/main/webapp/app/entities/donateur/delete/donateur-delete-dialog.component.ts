import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDonateur } from '../donateur.model';
import { DonateurService } from '../service/donateur.service';

@Component({
  templateUrl: './donateur-delete-dialog.component.html',
})
export class DonateurDeleteDialogComponent {
  donateur?: IDonateur;

  constructor(protected donateurService: DonateurService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.donateurService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
