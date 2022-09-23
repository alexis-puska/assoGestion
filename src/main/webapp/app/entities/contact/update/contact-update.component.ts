import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IContact, Contact } from '../contact.model';
import { ContactService } from '../service/contact.service';
import { IFamilleAccueil } from 'app/entities/famille-accueil/famille-accueil.model';
import { FamilleAccueilService } from 'app/entities/famille-accueil/service/famille-accueil.service';
import { IPointNourrissage } from 'app/entities/point-nourrissage/point-nourrissage.model';
import { PointNourrissageService } from 'app/entities/point-nourrissage/service/point-nourrissage.service';

@Component({
  selector: 'jhi-contact-update',
  templateUrl: './contact-update.component.html',
})
export class ContactUpdateComponent implements OnInit {
  isSaving = false;

  familleAccueilsSharedCollection: IFamilleAccueil[] = [];
  pointNourrissagesSharedCollection: IPointNourrissage[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required]],
    prenom: [null, [Validators.required]],
    mail: [],
    telMobile: [],
    telFixe: [],
    familleAccueil: [],
    pointNourrissage: [],
  });

  constructor(
    protected contactService: ContactService,
    protected familleAccueilService: FamilleAccueilService,
    protected pointNourrissageService: PointNourrissageService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contact }) => {
      this.updateForm(contact);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contact = this.createFromForm();
    if (contact.id !== undefined) {
      this.subscribeToSaveResponse(this.contactService.update(contact));
    } else {
      this.subscribeToSaveResponse(this.contactService.create(contact));
    }
  }

  trackFamilleAccueilById(index: number, item: IFamilleAccueil): number {
    return item.id!;
  }

  trackPointNourrissageById(index: number, item: IPointNourrissage): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContact>>): void {
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

  protected updateForm(contact: IContact): void {
    this.editForm.patchValue({
      id: contact.id,
      nom: contact.nom,
      prenom: contact.prenom,
      mail: contact.mail,
      telMobile: contact.telMobile,
      telFixe: contact.telFixe,
      familleAccueil: contact.familleAccueil,
      pointNourrissage: contact.pointNourrissage,
    });

    this.familleAccueilsSharedCollection = this.familleAccueilService.addFamilleAccueilToCollectionIfMissing(
      this.familleAccueilsSharedCollection,
      contact.familleAccueil
    );
    this.pointNourrissagesSharedCollection = this.pointNourrissageService.addPointNourrissageToCollectionIfMissing(
      this.pointNourrissagesSharedCollection,
      contact.pointNourrissage
    );
  }

  protected loadRelationshipsOptions(): void {
    this.familleAccueilService
      .query()
      .pipe(map((res: HttpResponse<IFamilleAccueil[]>) => res.body ?? []))
      .pipe(
        map((familleAccueils: IFamilleAccueil[]) =>
          this.familleAccueilService.addFamilleAccueilToCollectionIfMissing(familleAccueils, this.editForm.get('familleAccueil')!.value)
        )
      )
      .subscribe((familleAccueils: IFamilleAccueil[]) => (this.familleAccueilsSharedCollection = familleAccueils));

    this.pointNourrissageService
      .query()
      .pipe(map((res: HttpResponse<IPointNourrissage[]>) => res.body ?? []))
      .pipe(
        map((pointNourrissages: IPointNourrissage[]) =>
          this.pointNourrissageService.addPointNourrissageToCollectionIfMissing(
            pointNourrissages,
            this.editForm.get('pointNourrissage')!.value
          )
        )
      )
      .subscribe((pointNourrissages: IPointNourrissage[]) => (this.pointNourrissagesSharedCollection = pointNourrissages));
  }

  protected createFromForm(): IContact {
    return {
      ...new Contact(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      mail: this.editForm.get(['mail'])!.value,
      telMobile: this.editForm.get(['telMobile'])!.value,
      telFixe: this.editForm.get(['telFixe'])!.value,
      familleAccueil: this.editForm.get(['familleAccueil'])!.value,
      pointNourrissage: this.editForm.get(['pointNourrissage'])!.value,
    };
  }
}
