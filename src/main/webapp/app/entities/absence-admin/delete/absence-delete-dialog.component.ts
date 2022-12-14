import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAbsence } from '../../absence/absence.model';
import { AbsenceService } from '../../absence/service/absence.service';

@Component({
  templateUrl: './absence-delete-dialog.component.html',
})
export class AbsenceDeleteDialogComponent {
  absence?: IAbsence;

  constructor(protected absenceService: AbsenceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.absenceService.deleteAdmin(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
