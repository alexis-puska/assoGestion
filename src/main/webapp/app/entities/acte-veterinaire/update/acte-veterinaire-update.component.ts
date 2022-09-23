import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IActeVeterinaire, ActeVeterinaire } from '../acte-veterinaire.model';
import { ActeVeterinaireService } from '../service/acte-veterinaire.service';
import { IVisiteVeterinaire } from 'app/entities/visite-veterinaire/visite-veterinaire.model';
import { VisiteVeterinaireService } from 'app/entities/visite-veterinaire/service/visite-veterinaire.service';

@Component({
  selector: 'jhi-acte-veterinaire-update',
  templateUrl: './acte-veterinaire-update.component.html',
})
export class ActeVeterinaireUpdateComponent implements OnInit {
  isSaving = false;

  visiteVeterinairesSharedCollection: IVisiteVeterinaire[] = [];

  editForm = this.fb.group({
    id: [],
    libelle: [],
    visiteVeterinaire: [],
  });

  constructor(
    protected acteVeterinaireService: ActeVeterinaireService,
    protected visiteVeterinaireService: VisiteVeterinaireService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ acteVeterinaire }) => {
      this.updateForm(acteVeterinaire);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const acteVeterinaire = this.createFromForm();
    if (acteVeterinaire.id !== undefined) {
      this.subscribeToSaveResponse(this.acteVeterinaireService.update(acteVeterinaire));
    } else {
      this.subscribeToSaveResponse(this.acteVeterinaireService.create(acteVeterinaire));
    }
  }

  trackVisiteVeterinaireById(index: number, item: IVisiteVeterinaire): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IActeVeterinaire>>): void {
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

  protected updateForm(acteVeterinaire: IActeVeterinaire): void {
    this.editForm.patchValue({
      id: acteVeterinaire.id,
      libelle: acteVeterinaire.libelle,
      visiteVeterinaire: acteVeterinaire.visiteVeterinaire,
    });

    this.visiteVeterinairesSharedCollection = this.visiteVeterinaireService.addVisiteVeterinaireToCollectionIfMissing(
      this.visiteVeterinairesSharedCollection,
      acteVeterinaire.visiteVeterinaire
    );
  }

  protected loadRelationshipsOptions(): void {
    this.visiteVeterinaireService
      .query()
      .pipe(map((res: HttpResponse<IVisiteVeterinaire[]>) => res.body ?? []))
      .pipe(
        map((visiteVeterinaires: IVisiteVeterinaire[]) =>
          this.visiteVeterinaireService.addVisiteVeterinaireToCollectionIfMissing(
            visiteVeterinaires,
            this.editForm.get('visiteVeterinaire')!.value
          )
        )
      )
      .subscribe((visiteVeterinaires: IVisiteVeterinaire[]) => (this.visiteVeterinairesSharedCollection = visiteVeterinaires));
  }

  protected createFromForm(): IActeVeterinaire {
    return {
      ...new ActeVeterinaire(),
      id: this.editForm.get(['id'])!.value,
      libelle: this.editForm.get(['libelle'])!.value,
      visiteVeterinaire: this.editForm.get(['visiteVeterinaire'])!.value,
    };
  }
}
