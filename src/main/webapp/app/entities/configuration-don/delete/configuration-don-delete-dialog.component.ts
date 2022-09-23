import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IConfigurationDon } from '../configuration-don.model';
import { ConfigurationDonService } from '../service/configuration-don.service';

@Component({
  templateUrl: './configuration-don-delete-dialog.component.html',
})
export class ConfigurationDonDeleteDialogComponent {
  configurationDon?: IConfigurationDon;

  constructor(protected configurationDonService: ConfigurationDonService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.configurationDonService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
