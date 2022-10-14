import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAdresse } from 'app/entities/adresse/adresse.model';
import { FormeDonEnum } from 'app/entities/enumerations/forme-don-enum.model';
import { NatureDon } from 'app/entities/enumerations/nature-don.model';
import { NumeraireDonEnum } from 'app/entities/enumerations/numeraire-don-enum.model';
import { Donateur, IDonateur } from '../donateur.model';
import { DonateurService } from '../service/donateur.service';
import { NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { MomentDateFormatter } from 'app/shared/util/dateformat';

@Component({
  selector: 'jhi-donateur-update',
  templateUrl: './donateur-update.component.html',
  providers: [{ provide: NgbDateParserFormatter, useClass: MomentDateFormatter }],
})
export class DonateurUpdateComponent implements OnInit {
  isSaving = false;
  formeDonEnumValues = Object.keys(FormeDonEnum);
  natureDonValues = Object.keys(NatureDon);
  numeraireDonEnumValues = Object.keys(NumeraireDonEnum);

  adressesCollection: IAdresse[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required]],
    prenom: [null, [Validators.required]],
    montant: [null, [Validators.required]],
    sommeTouteLettre: [null, [Validators.required]],
    formeDon: [null, [Validators.required]],
    natureDon: [null, [Validators.required]],
    numeraireDon: [],
    adresse: this.fb.group({
      id: [],
      numero: [null, [Validators.required]],
      rue: [null, [Validators.required]],
      codePostale: [null, [Validators.required]],
      ville: [null, [Validators.required]],
    }),
    dateDon: [null, [Validators.required]],
  });

  constructor(protected donateurService: DonateurService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ donateur }) => {
      this.updateForm(donateur);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const donateur = this.createFromForm();
    if (donateur.id !== undefined) {
      this.subscribeToSaveResponse(this.donateurService.update(donateur));
    } else {
      this.subscribeToSaveResponse(this.donateurService.create(donateur));
    }
  }

  trackAdresseById(index: number, item: IAdresse): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDonateur>>): void {
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

  protected updateForm(donateur: IDonateur): void {
    this.editForm.patchValue({
      id: donateur.id,
      nom: donateur.nom,
      prenom: donateur.prenom,
      montant: donateur.montant,
      sommeTouteLettre: donateur.sommeTouteLettre,
      formeDon: donateur.formeDon,
      natureDon: donateur.natureDon,
      numeraireDon: donateur.numeraireDon,
      adresse: {
        id: donateur.adresse ? donateur.adresse.id : null,
        numero: donateur.adresse ? donateur.adresse.numero : null,
        rue: donateur.adresse ? donateur.adresse.rue : null,
        codePostale: donateur.adresse ? donateur.adresse.codePostale : null,
        ville: donateur.adresse ? donateur.adresse.ville : null,
      },
      dateDon: donateur.dateDon,
    });
  }

  protected createFromForm(): IDonateur {
    return {
      ...new Donateur(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      montant: this.editForm.get(['montant'])!.value,
      sommeTouteLettre: this.editForm.get(['sommeTouteLettre'])!.value,
      formeDon: this.editForm.get(['formeDon'])!.value,
      natureDon: this.editForm.get(['natureDon'])!.value,
      numeraireDon: this.editForm.get(['numeraireDon'])!.value,
      adresse: this.editForm.get(['adresse'])!.value,
      dateDon: this.editForm.get(['dateDon'])!.value,
    };
  }
}
