import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { VisiteVeterinaire, IVisiteVeterinaire } from 'app/entities/visite-veterinaire/visite-veterinaire.model';

@Component({
  selector: 'jhi-visite-veterinaire-update-dialog',
  templateUrl: './visite-veterinaire-update-dialog.component.html',
})
export class VisiteVeterinaireUpdateDialogComponent implements OnInit {
  isSaving = false;

  // needed to open modal
  visiteVeterinaire: VisiteVeterinaire | null = null;

  editForm = this.fb.group({
    id: [],
    chatId: [null, [Validators.required]],
    cliniqueVeterinaire: [null, [Validators.required]],
    dateVisite: [null, [Validators.required]],
    actes: [],
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
      chatId: visiteVeterinaire.chatId,
      cliniqueVeterinaire: visiteVeterinaire.cliniqueVeterinaire,
      dateVisite: visiteVeterinaire.dateVisite,
      actes: visiteVeterinaire.actes,
    });
  }

  protected createFromForm(): IVisiteVeterinaire {
    return {
      ...new VisiteVeterinaire(),
      id: this.editForm.get(['id'])!.value,
      chatId: this.editForm.get(['chatId'])!.value,
      cliniqueVeterinaire: this.editForm.get(['cliniqueVeterinaire'])!.value,
      dateVisite: this.editForm.get(['dateVisite'])!.value,
      actes: this.editForm.get(['actes'])!.value,
    };
  }
}
