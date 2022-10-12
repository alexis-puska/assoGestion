import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';

import { NgbActiveModal, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { ActeVeterinaire, IActeVeterinaire } from 'app/entities/acte-veterinaire/acte-veterinaire.model';
import { ActeVeterinaireEnum } from 'app/entities/enumerations/acte-veterinaire-enum.model';
import { MomentDateFormatter } from 'app/shared/util/dateformat';

@Component({
  selector: 'jhi-acte-veterinaire-update-dialog',
  templateUrl: './acte-veterinaire-update-dialog.component.html',
  providers: [{ provide: NgbDateParserFormatter, useClass: MomentDateFormatter }],
})
export class ActeVeterinaireUpdateDialogComponent implements OnInit {
  acteVeterinaireEnumValues = Object.keys(ActeVeterinaireEnum);

  isSaving = false;

  // needed to open modal
  acteVeterinaire: ActeVeterinaire | null = null;

  editForm = this.fb.group({
    id: [],
    libelle: [null, [Validators.required]],
  });

  constructor(protected fb: FormBuilder, public activeModal: NgbActiveModal) {}

  ngOnInit(): void {
    if (this.acteVeterinaire) {
      this.updateForm(this.acteVeterinaire);
    }
  }

  save(): void {
    this.isSaving = true;
    this.activeModal.close(this.createFromForm());
  }

  clean(): void {
    this.activeModal.dismiss();
  }

  protected updateForm(acteVeterinaire: IActeVeterinaire): void {
    this.editForm.patchValue({
      id: acteVeterinaire.id,
      libelle: acteVeterinaire.libelle,
    });
  }

  protected createFromForm(): IActeVeterinaire {
    return {
      ...new ActeVeterinaire(),
      id: this.editForm.get(['id'])!.value,
      libelle: this.editForm.get(['libelle'])!.value,
    };
  }
}
