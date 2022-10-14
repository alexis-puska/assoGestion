import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAdresse } from 'app/entities/adresse/adresse.model';
import { CliniqueVeterinaire, ICliniqueVeterinaire } from '../clinique-veterinaire.model';
import { CliniqueVeterinaireService } from '../service/clinique-veterinaire.service';

@Component({
  selector: 'jhi-clinique-veterinaire-update',
  templateUrl: './clinique-veterinaire-update.component.html',
})
export class CliniqueVeterinaireUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required]],
    actif: [],
    adresse: this.fb.group({
      id: [],
      numero: [null, [Validators.required]],
      rue: [null, [Validators.required]],
      codePostale: [null, [Validators.required]],
      ville: [null, [Validators.required]],
    }),
  });

  constructor(
    protected cliniqueVeterinaireService: CliniqueVeterinaireService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cliniqueVeterinaire }) => {
      this.updateForm(cliniqueVeterinaire);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cliniqueVeterinaire = this.createFromForm();
    if (cliniqueVeterinaire.id !== undefined) {
      this.subscribeToSaveResponse(this.cliniqueVeterinaireService.update(cliniqueVeterinaire));
    } else {
      this.subscribeToSaveResponse(this.cliniqueVeterinaireService.create(cliniqueVeterinaire));
    }
  }

  trackAdresseById(index: number, item: IAdresse): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICliniqueVeterinaire>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(cliniqueVeterinaire: ICliniqueVeterinaire): void {
    this.editForm.patchValue({
      id: cliniqueVeterinaire.id,
      nom: cliniqueVeterinaire.nom,
      actif: cliniqueVeterinaire.actif,
      adresse: {
        id: cliniqueVeterinaire.adresse ? cliniqueVeterinaire.adresse.id : null,
        numero: cliniqueVeterinaire.adresse ? cliniqueVeterinaire.adresse.numero : null,
        rue: cliniqueVeterinaire.adresse ? cliniqueVeterinaire.adresse.rue : null,
        codePostale: cliniqueVeterinaire.adresse ? cliniqueVeterinaire.adresse.codePostale : null,
        ville: cliniqueVeterinaire.adresse ? cliniqueVeterinaire.adresse.ville : null,
      },
    });
  }

  protected createFromForm(): ICliniqueVeterinaire {
    return {
      ...new CliniqueVeterinaire(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      actif: this.editForm.get(['actif'])!.value,
      adresse: this.editForm.get(['adresse'])!.value,
    };
  }
}
