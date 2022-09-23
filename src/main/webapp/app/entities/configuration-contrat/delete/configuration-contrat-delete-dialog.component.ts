import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IConfigurationContrat } from '../configuration-contrat.model';
import { ConfigurationContratService } from '../service/configuration-contrat.service';

@Component({
  templateUrl: './configuration-contrat-delete-dialog.component.html',
})
export class ConfigurationContratDeleteDialogComponent {
  configurationContrat?: IConfigurationContrat;

  constructor(protected configurationContratService: ConfigurationContratService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.configurationContratService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
