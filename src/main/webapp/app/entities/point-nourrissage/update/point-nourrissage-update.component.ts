import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAdresse } from 'app/entities/adresse/adresse.model';
import { IPointNourrissage, PointNourrissage } from '../point-nourrissage.model';
import { PointNourrissageService } from '../service/point-nourrissage.service';

@Component({
  selector: 'jhi-point-nourrissage-update',
  templateUrl: './point-nourrissage-update.component.html',
})
export class PointNourrissageUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required]],
    contacts: [[]],
    adresse: this.fb.group({
      id: [],
      numero: [null, [Validators.required]],
      rue: [null, [Validators.required]],
      codePostale: [null, [Validators.required]],
      ville: [null, [Validators.required]],
    }),
  });

  constructor(
    protected pointNourrissageService: PointNourrissageService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pointNourrissage }) => {
      this.updateForm(pointNourrissage);
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
      contacts: pointNourrissage.contacts,
      adresse: {
        id: pointNourrissage.adresse ? pointNourrissage.adresse.id : null,
        numero: pointNourrissage.adresse ? pointNourrissage.adresse.numero : null,
        rue: pointNourrissage.adresse ? pointNourrissage.adresse.rue : null,
        codePostale: pointNourrissage.adresse ? pointNourrissage.adresse.codePostale : null,
        ville: pointNourrissage.adresse ? pointNourrissage.adresse.ville : null,
      },
    });
  }

  protected createFromForm(): IPointNourrissage {
    return {
      ...new PointNourrissage(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      adresse: this.editForm.get(['adresse'])!.value,
      contacts: this.editForm.get(['contacts'])!.value,
    };
  }
}
