import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPointNourrissage, PointNourrissage } from '../point-nourrissage.model';
import { PointNourrissageService } from '../service/point-nourrissage.service';
import { IAdresse } from 'app/entities/adresse/adresse.model';
import { AdresseService } from 'app/entities/adresse/service/adresse.service';

@Component({
  selector: 'jhi-point-nourrissage-update',
  templateUrl: './point-nourrissage-update.component.html',
})
export class PointNourrissageUpdateComponent implements OnInit {
  isSaving = false;

  adressesCollection: IAdresse[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [],
    adresse: [],
  });

  constructor(
    protected pointNourrissageService: PointNourrissageService,
    protected adresseService: AdresseService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pointNourrissage }) => {
      this.updateForm(pointNourrissage);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pointNourrissage = this.createFromForm();
    if (pointNourrissage.id !== undefined) {
      this.subscribeToSaveResponse(this.pointNourrissageService.update(pointNourrissage));
    } else {
      this.subscribeToSaveResponse(this.pointNourrissageService.create(pointNourrissage));
    }
  }

  trackAdresseById(index: number, item: IAdresse): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPointNourrissage>>): void {
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

  protected updateForm(pointNourrissage: IPointNourrissage): void {
    this.editForm.patchValue({
      id: pointNourrissage.id,
      nom: pointNourrissage.nom,
      adresse: pointNourrissage.adresse,
    });

    this.adressesCollection = this.adresseService.addAdresseToCollectionIfMissing(this.adressesCollection, pointNourrissage.adresse);
  }

  protected loadRelationshipsOptions(): void {
    this.adresseService
      .query({ filter: 'pointnourrissage-is-null' })
      .pipe(map((res: HttpResponse<IAdresse[]>) => res.body ?? []))
      .pipe(
        map((adresses: IAdresse[]) => this.adresseService.addAdresseToCollectionIfMissing(adresses, this.editForm.get('adresse')!.value))
      )
      .subscribe((adresses: IAdresse[]) => (this.adressesCollection = adresses));
  }

  protected createFromForm(): IPointNourrissage {
    return {
      ...new PointNourrissage(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      adresse: this.editForm.get(['adresse'])!.value,
    };
  }
}
