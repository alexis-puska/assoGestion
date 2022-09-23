import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFamilleAccueil, FamilleAccueil } from '../famille-accueil.model';
import { FamilleAccueilService } from '../service/famille-accueil.service';
import { IAdresse } from 'app/entities/adresse/adresse.model';
import { AdresseService } from 'app/entities/adresse/service/adresse.service';
import { TypeLogementEnum } from 'app/entities/enumerations/type-logement-enum.model';

@Component({
  selector: 'jhi-famille-accueil-update',
  templateUrl: './famille-accueil-update.component.html',
})
export class FamilleAccueilUpdateComponent implements OnInit {
  isSaving = false;
  typeLogementEnumValues = Object.keys(TypeLogementEnum);

  adressesCollection: IAdresse[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [],
    typeLogement: [],
    nombrePiece: [],
    nombreChat: [],
    nombreChien: [],
    adresse: [],
  });

  constructor(
    protected familleAccueilService: FamilleAccueilService,
    protected adresseService: AdresseService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ familleAccueil }) => {
      this.updateForm(familleAccueil);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const familleAccueil = this.createFromForm();
    if (familleAccueil.id !== undefined) {
      this.subscribeToSaveResponse(this.familleAccueilService.update(familleAccueil));
    } else {
      this.subscribeToSaveResponse(this.familleAccueilService.create(familleAccueil));
    }
  }

  trackAdresseById(index: number, item: IAdresse): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFamilleAccueil>>): void {
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

  protected updateForm(familleAccueil: IFamilleAccueil): void {
    this.editForm.patchValue({
      id: familleAccueil.id,
      nom: familleAccueil.nom,
      typeLogement: familleAccueil.typeLogement,
      nombrePiece: familleAccueil.nombrePiece,
      nombreChat: familleAccueil.nombreChat,
      nombreChien: familleAccueil.nombreChien,
      adresse: familleAccueil.adresse,
    });

    this.adressesCollection = this.adresseService.addAdresseToCollectionIfMissing(this.adressesCollection, familleAccueil.adresse);
  }

  protected loadRelationshipsOptions(): void {
    this.adresseService
      .query({ filter: 'familleaccueil-is-null' })
      .pipe(map((res: HttpResponse<IAdresse[]>) => res.body ?? []))
      .pipe(
        map((adresses: IAdresse[]) => this.adresseService.addAdresseToCollectionIfMissing(adresses, this.editForm.get('adresse')!.value))
      )
      .subscribe((adresses: IAdresse[]) => (this.adressesCollection = adresses));
  }

  protected createFromForm(): IFamilleAccueil {
    return {
      ...new FamilleAccueil(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      typeLogement: this.editForm.get(['typeLogement'])!.value,
      nombrePiece: this.editForm.get(['nombrePiece'])!.value,
      nombreChat: this.editForm.get(['nombreChat'])!.value,
      nombreChien: this.editForm.get(['nombreChien'])!.value,
      adresse: this.editForm.get(['adresse'])!.value,
    };
  }
}
