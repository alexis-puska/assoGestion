import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAdresse } from 'app/entities/adresse/adresse.model';
import { AdresseService } from 'app/entities/adresse/service/adresse.service';
import { TypeLogementEnum } from 'app/entities/enumerations/type-logement-enum.model';
import { FamilleAccueil, IFamilleAccueil } from '../famille-accueil.model';
import { FamilleAccueilService } from '../service/famille-accueil.service';
import { Contact } from './../../contact/contact.model';

@Component({
  selector: 'jhi-famille-accueil-update',
  templateUrl: './famille-accueil-update.component.html',
})
export class FamilleAccueilUpdateComponent implements OnInit {
  isSaving = false;
  typeLogementEnumValues = Object.keys(TypeLogementEnum);

  contacts: Contact[] | null = [];

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required]],
    typeLogement: [],
    nombrePiece: [],
    nombreChat: [],
    nombreChien: [],
    adresse: this.fb.group({
      id: [],
      numero: [null, [Validators.required]],
      rue: [null, [Validators.required]],
      codePostale: [null, [Validators.required]],
      ville: [null, [Validators.required]],
    }),
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
      adresse: {
        id: familleAccueil.adresse ? familleAccueil.adresse.id : null,
        numero: familleAccueil.adresse ? familleAccueil.adresse.numero : null,
        rue: familleAccueil.adresse ? familleAccueil.adresse.rue : null,
        codePostale: familleAccueil.adresse ? familleAccueil.adresse.codePostale : null,
        ville: familleAccueil.adresse ? familleAccueil.adresse.ville : null,
      },
    });
    this.contacts = familleAccueil.contacts ? familleAccueil.contacts : [];
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
      contacts: this.contacts,
    };
  }
}
