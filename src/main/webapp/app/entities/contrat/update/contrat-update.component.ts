import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IContrat, Contrat } from '../contrat.model';
import { ContratService } from '../service/contrat.service';
import { IAdresse } from 'app/entities/adresse/adresse.model';
import { AdresseService } from 'app/entities/adresse/service/adresse.service';
import { PaiementEnum } from 'app/entities/enumerations/paiement-enum.model';

@Component({
  selector: 'jhi-contrat-update',
  templateUrl: './contrat-update.component.html',
})
export class ContratUpdateComponent implements OnInit {
  isSaving = false;
  paiementEnumValues = Object.keys(PaiementEnum);

  adresseAdoptantsCollection: IAdresse[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [],
    prenom: [],
    cout: [],
    paiement: [],
    dateContrat: [],
    adresseAdoptant: [],
  });

  constructor(
    protected contratService: ContratService,
    protected adresseService: AdresseService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contrat }) => {
      this.updateForm(contrat);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contrat = this.createFromForm();
    if (contrat.id !== undefined) {
      this.subscribeToSaveResponse(this.contratService.update(contrat));
    } else {
      this.subscribeToSaveResponse(this.contratService.create(contrat));
    }
  }

  trackAdresseById(index: number, item: IAdresse): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContrat>>): void {
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

  protected updateForm(contrat: IContrat): void {
    this.editForm.patchValue({
      id: contrat.id,
      nom: contrat.nom,
      prenom: contrat.prenom,
      cout: contrat.cout,
      paiement: contrat.paiement,
      dateContrat: contrat.dateContrat,
      adresseAdoptant: contrat.adresseAdoptant,
    });

    this.adresseAdoptantsCollection = this.adresseService.addAdresseToCollectionIfMissing(
      this.adresseAdoptantsCollection,
      contrat.adresseAdoptant
    );
  }

  protected loadRelationshipsOptions(): void {
    this.adresseService
      .query({ filter: 'contrat-is-null' })
      .pipe(map((res: HttpResponse<IAdresse[]>) => res.body ?? []))
      .pipe(
        map((adresses: IAdresse[]) =>
          this.adresseService.addAdresseToCollectionIfMissing(adresses, this.editForm.get('adresseAdoptant')!.value)
        )
      )
      .subscribe((adresses: IAdresse[]) => (this.adresseAdoptantsCollection = adresses));
  }

  protected createFromForm(): IContrat {
    return {
      ...new Contrat(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      cout: this.editForm.get(['cout'])!.value,
      paiement: this.editForm.get(['paiement'])!.value,
      dateContrat: this.editForm.get(['dateContrat'])!.value,
      adresseAdoptant: this.editForm.get(['adresseAdoptant'])!.value,
    };
  }
}
