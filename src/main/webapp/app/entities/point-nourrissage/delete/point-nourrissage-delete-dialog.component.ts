import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPointNourrissage } from '../point-nourrissage.model';
import { PointNourrissageService } from '../service/point-nourrissage.service';

@Component({
  templateUrl: './point-nourrissage-delete-dialog.component.html',
})
export class PointNourrissageDeleteDialogComponent {
  pointNourrissage?: IPointNourrissage;

  constructor(protected pointNourrissageService: PointNourrissageService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pointNourrissageService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
