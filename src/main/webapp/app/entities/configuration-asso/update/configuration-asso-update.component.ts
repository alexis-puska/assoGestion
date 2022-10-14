import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ConfigurationAsso, IConfigurationAsso } from '../configuration-asso.model';
import { ConfigurationAssoService } from '../service/configuration-asso.service';
import { LocalStorageService, SessionStorageService } from 'ngx-webstorage';

@Component({
  selector: 'jhi-configuration-asso-update',
  templateUrl: './configuration-asso-update.component.html',
})
export class ConfigurationAssoUpdateComponent implements OnInit {
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
    siret: [null, Validators.maxLength(17)],
    email: [null, Validators.maxLength(255)],
    telephone: [null, Validators.maxLength(20)],
    adresse: this.fb.group({
      id: [],
      numero: [null, [Validators.required]],
      rue: [null, [Validators.required]],
      codePostale: [null, [Validators.required]],
      ville: [null, [Validators.required]],
    }),
  });

  constructor(
    protected configurationAssoService: ConfigurationAssoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    private localStorage: LocalStorageService,
    private sessionStorage: SessionStorageService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ configurationAsso }) => {
      this.updateForm(configurationAsso);
      const token = this.localStorage.retrieve('authenticationToken') || this.sessionStorage.retrieve('authenticationToken');
      this.url = `api/configuration-assos/signature?Authorization=Bearer ${token}`;
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const configurationAsso = this.createFromForm();
    this.subscribeToSaveResponse(this.configurationAssoService.update(configurationAsso, this.selectedFile));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConfigurationAsso>>): void {
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

  protected updateForm(configurationAsso: IConfigurationAsso): void {
    this.hasSignature = configurationAsso.hasSignature ? configurationAsso.hasSignature : false;
    this.editForm.patchValue({
      id: configurationAsso.id,
      denomination: configurationAsso.denomination,
      objet: configurationAsso.objet,
      objet1: configurationAsso.objet1,
      objet2: configurationAsso.objet2,
      siret: configurationAsso.siret,
      email: configurationAsso.email,
      telephone: configurationAsso.telephone,
      adresse: {
        id: configurationAsso.adresse ? configurationAsso.adresse.id : null,
        numero: configurationAsso.adresse ? configurationAsso.adresse.numero : null,
        rue: configurationAsso.adresse ? configurationAsso.adresse.rue : null,
        codePostale: configurationAsso.adresse ? configurationAsso.adresse.codePostale : null,
        ville: configurationAsso.adresse ? configurationAsso.adresse.ville : null,
      },
    });
  }

  protected createFromForm(): IConfigurationAsso {
    return {
      ...new ConfigurationAsso(),
      id: this.editForm.get(['id'])!.value,
      denomination: this.editForm.get(['denomination'])!.value,
      objet: this.editForm.get(['objet'])!.value,
      objet1: this.editForm.get(['objet1'])!.value,
      objet2: this.editForm.get(['objet2'])!.value,
      siret: this.editForm.get(['siret'])!.value,
      email: this.editForm.get(['email'])!.value,
      telephone: this.editForm.get(['telephone'])!.value,
      adresse: this.editForm.get(['adresse'])!.value,
      deleteSignature: this.deleteSignature,
    };
  }
}
