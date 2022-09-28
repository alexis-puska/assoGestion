import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';

import { NgbActiveModal, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { ActeVeterinaire } from 'app/entities/acte-veterinaire/acte-veterinaire.model';
import { IVisiteVeterinaire, VisiteVeterinaire } from 'app/entities/visite-veterinaire/visite-veterinaire.model';
import { MomentDateFormatter } from 'app/shared/util/dateformat';

@Component({
  selector: 'jhi-visite-veterinaire-update-dialog',
  templateUrl: './visite-veterinaire-update-dialog.component.html',
  providers: [{ provide: NgbDateParserFormatter, useClass: MomentDateFormatter }],
})
export class VisiteVeterinaireUpdateDialogComponent implements OnInit {
  isSaving = false;

  // needed to open modal
  visiteVeterinaire: VisiteVeterinaire | null = null;
  actes: ActeVeterinaire[] = [];

  editForm = this.fb.group({
    id: [],
    cliniqueVeterinaire: [null, [Validators.required]],
    dateVisite: [null, [Validators.required]],
  });

  constructor(protected fb: FormBuilder, public activeModal: NgbActiveModal) {}

  ngOnInit(): void {
    if (this.visiteVeterinaire) {
      this.updateForm(this.visiteVeterinaire);
    }
  }

  save(): void {
    this.isSaving = true;
    this.activeModal.close(this.createFromForm());
  }

  clean(): void {
    this.activeModal.dismiss();
  }

  protected updateForm(visiteVeterinaire: IVisiteVeterinaire): void {
    this.editForm.patchValue({
      id: visiteVeterinaire.id,
      cliniqueVeterinaire: visiteVeterinaire.cliniqueVeterinaire,
      dateVisite: visiteVeterinaire.dateVisite,
    });
    if (visiteVeterinaire.actes) {
      this.actes = visiteVeterinaire.actes;
    } else {
      this.actes = [];
    }
  }

  protected createFromForm(): IVisiteVeterinaire {
    const visite = {
      ...new VisiteVeterinaire(),
      id: this.editForm.get(['id'])!.value,
      cliniqueVeterinaire: this.editForm.get(['cliniqueVeterinaire'])!.value,
      dateVisite: this.editForm.get(['dateVisite'])!.value,
    };
    visite.actes = this.actes;
    return visite;
  }
}
