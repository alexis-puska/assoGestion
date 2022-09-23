import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPointCapture } from '../point-capture.model';
import { PointCaptureService } from '../service/point-capture.service';

@Component({
  templateUrl: './point-capture-delete-dialog.component.html',
})
export class PointCaptureDeleteDialogComponent {
  pointCapture?: IPointCapture;

  constructor(protected pointCaptureService: PointCaptureService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pointCaptureService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
