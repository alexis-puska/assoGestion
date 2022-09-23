import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPointCapture, PointCapture } from '../point-capture.model';
import { PointCaptureService } from '../service/point-capture.service';
import { IAdresse } from 'app/entities/adresse/adresse.model';
import { AdresseService } from 'app/entities/adresse/service/adresse.service';

@Component({
  selector: 'jhi-point-capture-update',
  templateUrl: './point-capture-update.component.html',
})
export class PointCaptureUpdateComponent implements OnInit {
  isSaving = false;

  adresseCapturesCollection: IAdresse[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [],
    adresseCapture: [],
  });

  constructor(
    protected pointCaptureService: PointCaptureService,
    protected adresseService: AdresseService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pointCapture }) => {
      this.updateForm(pointCapture);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pointCapture = this.createFromForm();
    if (pointCapture.id !== undefined) {
      this.subscribeToSaveResponse(this.pointCaptureService.update(pointCapture));
    } else {
      this.subscribeToSaveResponse(this.pointCaptureService.create(pointCapture));
    }
  }

  trackAdresseById(index: number, item: IAdresse): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPointCapture>>): void {
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

  protected updateForm(pointCapture: IPointCapture): void {
    this.editForm.patchValue({
      id: pointCapture.id,
      nom: pointCapture.nom,
      adresseCapture: pointCapture.adresseCapture,
    });

    this.adresseCapturesCollection = this.adresseService.addAdresseToCollectionIfMissing(
      this.adresseCapturesCollection,
      pointCapture.adresseCapture
    );
  }

  protected loadRelationshipsOptions(): void {
    this.adresseService
      .query({ filter: 'pointcapture-is-null' })
      .pipe(map((res: HttpResponse<IAdresse[]>) => res.body ?? []))
      .pipe(
        map((adresses: IAdresse[]) =>
          this.adresseService.addAdresseToCollectionIfMissing(adresses, this.editForm.get('adresseCapture')!.value)
        )
      )
      .subscribe((adresses: IAdresse[]) => (this.adresseCapturesCollection = adresses));
  }

  protected createFromForm(): IPointCapture {
    return {
      ...new PointCapture(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      adresseCapture: this.editForm.get(['adresseCapture'])!.value,
    };
  }
}
