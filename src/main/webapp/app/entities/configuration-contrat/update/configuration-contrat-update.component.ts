import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IConfigurationContrat, ConfigurationContrat } from '../configuration-contrat.model';
import { ConfigurationContratService } from '../service/configuration-contrat.service';

@Component({
  selector: 'jhi-configuration-contrat-update',
  templateUrl: './configuration-contrat-update.component.html',
})
export class ConfigurationContratUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    content: [],
  });

  constructor(
    protected configurationContratService: ConfigurationContratService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ configurationContrat }) => {
      this.updateForm(configurationContrat);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const configurationContrat = this.createFromForm();
    if (configurationContrat.id !== undefined) {
      this.subscribeToSaveResponse(this.configurationContratService.update(configurationContrat));
    } else {
      this.subscribeToSaveResponse(this.configurationContratService.create(configurationContrat));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConfigurationContrat>>): void {
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

  protected updateForm(configurationContrat: IConfigurationContrat): void {
    this.editForm.patchValue({
      id: configurationContrat.id,
      content: configurationContrat.content,
    });
  }

  protected createFromForm(): IConfigurationContrat {
    return {
      ...new ConfigurationContrat(),
      id: this.editForm.get(['id'])!.value,
      content: this.editForm.get(['content'])!.value,
    };
  }
}
