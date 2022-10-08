import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { AdresseService } from 'app/entities/adresse/service/adresse.service';
import { ConfigurationDon, IConfigurationDon } from '../configuration-don.model';
import { ConfigurationDonService } from '../service/configuration-don.service';
import { LocalStorageService, SessionStorageService } from 'ngx-webstorage';

@Component({
  selector: 'jhi-configuration-don-update',
  templateUrl: './configuration-don-update.component.html',
})
export class ConfigurationDonUpdateComponent implements OnInit {
  isSaving = false;

  hasSignature = false;
  deleteSignature = false;
  signatureSizeLimit = false;
  selectedFile: File | undefined;
  url: string | ArrayBuffer | null = null;

  editForm = this.fb.group({
    id: [],
    denomination: [],
    objet: [null, Validators.maxLength(128)],
    objet1: [null, Validators.maxLength(128)],
    objet2: [null, Validators.maxLength(128)],
    adresse: this.fb.group({
      id: [],
      numero: [null, [Validators.required]],
      rue: [null, [Validators.required]],
      codePostale: [null, [Validators.required]],
      ville: [null, [Validators.required]],
    }),
  });

  constructor(
    protected configurationDonService: ConfigurationDonService,
    protected adresseService: AdresseService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    private localStorage: LocalStorageService,
    private sessionStorage: SessionStorageService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ configurationDon }) => {
      this.updateForm(configurationDon);
      const token = this.localStorage.retrieve('authenticationToken') || this.sessionStorage.retrieve('authenticationToken');
      this.url = `api/configuration-dons/signature?Authorization=Bearer ${token}`;
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const configurationDon = this.createFromForm();
    this.subscribeToSaveResponse(this.configurationDonService.update(configurationDon, this.selectedFile));
  }

  removeSignature(): void {
    this.hasSignature = false;
    this.deleteSignature = true;
  }

  onFileSelected($event: any): void {
    this.url = null;
    this.selectedFile = $event?.target?.files[0];
    if ($event?.target?.files[0]) {
      const reader = new FileReader();
      reader.readAsDataURL($event?.target?.files[0]);
      reader.onload = () => {
        this.url = reader.result;
      };
    }
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
    this.hasSignature = configurationDon.hasSignature ? configurationDon.hasSignature : false;
    this.editForm.patchValue({
      id: configurationDon.id,
      denomination: configurationDon.denomination,
      objet: configurationDon.objet,
      objet1: configurationDon.objet1,
      objet2: configurationDon.objet2,
      adresse: {
        id: configurationDon.adresse ? configurationDon.adresse.id : null,
        numero: configurationDon.adresse ? configurationDon.adresse.numero : null,
        rue: configurationDon.adresse ? configurationDon.adresse.rue : null,
        codePostale: configurationDon.adresse ? configurationDon.adresse.codePostale : null,
        ville: configurationDon.adresse ? configurationDon.adresse.ville : null,
      },
    });
  }

  protected createFromForm(): IConfigurationDon {
    return {
      ...new ConfigurationDon(),
      id: this.editForm.get(['id'])!.value,
      denomination: this.editForm.get(['denomination'])!.value,
      objet: this.editForm.get(['objet'])!.value,
      objet1: this.editForm.get(['objet1'])!.value,
      objet2: this.editForm.get(['objet2'])!.value,
      adresse: this.editForm.get(['adresse'])!.value,
      deleteSignature: this.deleteSignature,
    };
  }
}
