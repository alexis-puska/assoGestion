import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICliniqueVeterinaire, CliniqueVeterinaire } from '../clinique-veterinaire.model';
import { CliniqueVeterinaireService } from '../service/clinique-veterinaire.service';
import { IAdresse } from 'app/entities/adresse/adresse.model';
import { AdresseService } from 'app/entities/adresse/service/adresse.service';

@Component({
  selector: 'jhi-clinique-veterinaire-update',
  templateUrl: './clinique-veterinaire-update.component.html',
})
export class CliniqueVeterinaireUpdateComponent implements OnInit {
  isSaving = false;

  adressesCollection: IAdresse[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [],
    actif: [],
    adresse: [],
  });

  constructor(
    protected cliniqueVeterinaireService: CliniqueVeterinaireService,
    protected adresseService: AdresseService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cliniqueVeterinaire }) => {
      this.updateForm(cliniqueVeterinaire);

      this.loadRelationshipsOptions();
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
      adresse: cliniqueVeterinaire.adresse,
    });

    this.adressesCollection = this.adresseService.addAdresseToCollectionIfMissing(this.adressesCollection, cliniqueVeterinaire.adresse);
  }

  protected loadRelationshipsOptions(): void {
    this.adresseService
      .query({ filter: 'cliniqueveterinaire-is-null' })
      .pipe(map((res: HttpResponse<IAdresse[]>) => res.body ?? []))
      .pipe(
        map((adresses: IAdresse[]) => this.adresseService.addAdresseToCollectionIfMissing(adresses, this.editForm.get('adresse')!.value))
      )
      .subscribe((adresses: IAdresse[]) => (this.adressesCollection = adresses));
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
