import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IConfigurationDon, ConfigurationDon } from '../configuration-don.model';
import { ConfigurationDonService } from '../service/configuration-don.service';
import { IAdresse } from 'app/entities/adresse/adresse.model';
import { AdresseService } from 'app/entities/adresse/service/adresse.service';

@Component({
  selector: 'jhi-configuration-don-update',
  templateUrl: './configuration-don-update.component.html',
})
export class ConfigurationDonUpdateComponent implements OnInit {
  isSaving = false;

  adressesCollection: IAdresse[] = [];

  editForm = this.fb.group({
    id: [],
    denomination: [],
    objet: [],
    signataire: [],
    adresse: [],
  });

  constructor(
    protected configurationDonService: ConfigurationDonService,
    protected adresseService: AdresseService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ configurationDon }) => {
      this.updateForm(configurationDon);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const configurationDon = this.createFromForm();
    if (configurationDon.id !== undefined) {
      this.subscribeToSaveResponse(this.configurationDonService.update(configurationDon));
    } else {
      this.subscribeToSaveResponse(this.configurationDonService.create(configurationDon));
    }
  }

  trackAdresseById(index: number, item: IAdresse): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConfigurationDon>>): void {
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

  protected updateForm(configurationDon: IConfigurationDon): void {
    this.editForm.patchValue({
      id: configurationDon.id,
      denomination: configurationDon.denomination,
      objet: configurationDon.objet,
      signataire: configurationDon.signataire,
      adresse: configurationDon.adresse,
    });

    this.adressesCollection = this.adresseService.addAdresseToCollectionIfMissing(this.adressesCollection, configurationDon.adresse);
  }

  protected loadRelationshipsOptions(): void {
    this.adresseService
      .query({ filter: 'configurationdon-is-null' })
      .pipe(map((res: HttpResponse<IAdresse[]>) => res.body ?? []))
      .pipe(
        map((adresses: IAdresse[]) => this.adresseService.addAdresseToCollectionIfMissing(adresses, this.editForm.get('adresse')!.value))
      )
      .subscribe((adresses: IAdresse[]) => (this.adressesCollection = adresses));
  }

  protected createFromForm(): IConfigurationDon {
    return {
      ...new ConfigurationDon(),
      id: this.editForm.get(['id'])!.value,
      denomination: this.editForm.get(['denomination'])!.value,
      objet: this.editForm.get(['objet'])!.value,
      signataire: this.editForm.get(['signataire'])!.value,
      adresse: this.editForm.get(['adresse'])!.value,
    };
  }
}
