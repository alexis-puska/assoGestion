import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { IContrat } from 'app/entities/contrat/contrat.model';
import { ContratService } from 'app/entities/contrat/service/contrat.service';
import { PaiementEnum } from 'app/entities/enumerations/paiement-enum.model';
import { PoilEnum } from 'app/entities/enumerations/poil-enum.model';
import { TypeIdentificationEnum } from 'app/entities/enumerations/type-identification-enum.model';
import { FamilleAccueilService } from 'app/entities/famille-accueil/service/famille-accueil.service';
import { PointCaptureService } from 'app/entities/point-capture/service/point-capture.service';
import { RaceChatService } from 'app/entities/race-chat/service/race-chat.service';
import { VisiteVeterinaire } from 'app/entities/visite-veterinaire/visite-veterinaire.model';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { MomentDateFormatter } from 'app/shared/util/dateformat';
import { Chat, IChat } from '../chat.model';
import { ChatService } from '../service/chat.service';

@Component({
  selector: 'jhi-chat-update',
  templateUrl: './chat-update.component.html',
  providers: [{ provide: NgbDateParserFormatter, useClass: MomentDateFormatter }],
})
export class ChatUpdateComponent implements OnInit {
  isSaving = false;
  typeIdentificationEnumValues = Object.keys(TypeIdentificationEnum);
  poilEnumValues = Object.keys(PoilEnum);
  paiementEnumValues = Object.keys(PaiementEnum);

  visites: VisiteVeterinaire[] | null = [];
  possedeContrat = false;

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required]],
    typeIdentification: [],
    identification: [],
    dateNaissance: [null, [Validators.required]],
    description: [],
    robe: [null, [Validators.required]],
    poil: [null, [Validators.required]],
    famille: [],
    adresseCapture: [],
    race: [],
    contrat: this.fb.group({
      id: [],
      nom: [],
      prenom: [],
      cout: [],
      paiement: [],
      dateContrat: [],
      adresseAdoptant: this.fb.group({
        id: [],
        numero: [],
        rue: [],
        codePostale: [],
        ville: [],
      }),
    }),
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected chatService: ChatService,
    protected contratService: ContratService,
    protected familleAccueilService: FamilleAccueilService,
    protected pointCaptureService: PointCaptureService,
    protected raceChatService: RaceChatService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ chat }) => {
      this.updateForm(chat);
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('assoGestionApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const chat = this.createFromForm();
    if (chat.id !== undefined && chat.id !== null) {
      this.subscribeToSaveResponse(this.chatService.update(chat));
    } else {
      this.subscribeToSaveResponse(this.chatService.create(chat));
    }
  }

  trackContratById(index: number, item: IContrat): number {
    return item.id!;
  }

  addValidatorContrat(): void {
    this.editForm.get('contrat.nom')?.setValidators([Validators.required]);
    this.editForm.get('contrat.prenom')?.setValidators([Validators.required]);
    this.editForm.get('contrat.cout')?.setValidators([Validators.required]);
    this.editForm.get('contrat.paiement')?.setValidators([Validators.required]);
    this.editForm.get('contrat.dateContrat')?.setValidators([Validators.required]);
    this.editForm.get('contrat.adresseAdoptant.numero')?.setValidators([Validators.required]);
    this.editForm.get('contrat.adresseAdoptant.rue')?.setValidators([Validators.required]);
    this.editForm.get('contrat.adresseAdoptant.codePostale')?.setValidators([Validators.required]);
    this.editForm.get('contrat.adresseAdoptant.ville')?.setValidators([Validators.required]);
    this.refreshValueAndValidity();
    this.possedeContrat = true;
  }

  removeValidatorContrat(): void {
    this.editForm.get('contrat.nom')?.clearValidators();
    this.editForm.get('contrat.prenom')?.clearValidators();
    this.editForm.get('contrat.cout')?.clearValidators();
    this.editForm.get('contrat.paiement')?.clearValidators();
    this.editForm.get('contrat.dateContrat')?.clearValidators();
    this.editForm.get('contrat.adresseAdoptant.numero')?.clearValidators();
    this.editForm.get('contrat.adresseAdoptant.rue')?.clearValidators();
    this.editForm.get('contrat.adresseAdoptant.codePostale')?.clearValidators();
    this.editForm.get('contrat.adresseAdoptant.ville')?.clearValidators();
    this.refreshValueAndValidity();
    this.possedeContrat = false;
  }

  refreshValueAndValidity(): void {
    this.editForm.get('contrat.nom')?.updateValueAndValidity();
    this.editForm.get('contrat.prenom')?.updateValueAndValidity();
    this.editForm.get('contrat.cout')?.updateValueAndValidity();
    this.editForm.get('contrat.paiement')?.updateValueAndValidity();
    this.editForm.get('contrat.dateContrat')?.updateValueAndValidity();
    this.editForm.get('contrat.adresseAdoptant.numero')?.updateValueAndValidity();
    this.editForm.get('contrat.adresseAdoptant.rue')?.updateValueAndValidity();
    this.editForm.get('contrat.adresseAdoptant.codePostale')?.updateValueAndValidity();
    this.editForm.get('contrat.adresseAdoptant.ville')?.updateValueAndValidity();
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChat>>): void {
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

  protected updateForm(chat: IChat): void {
    if (!chat.contrat) {
      this.possedeContrat = false;
      this.removeValidatorContrat();
      this.editForm.patchValue({
        id: chat.id,
        nom: chat.nom,
        typeIdentification: chat.typeIdentification,
        identification: chat.identification,
        dateNaissance: chat.dateNaissance,
        description: chat.description,
        robe: chat.robe,
        poil: chat.poil,
        famille: chat.famille,
        adresseCapture: chat.adresseCapture,
        race: chat.race,
      });
    } else {
      this.editForm.patchValue({
        id: chat.id,
        nom: chat.nom,
        typeIdentification: chat.typeIdentification,
        identification: chat.identification,
        dateNaissance: chat.dateNaissance,
        description: chat.description,
        robe: chat.robe,
        poil: chat.poil,
        famille: chat.famille,
        adresseCapture: chat.adresseCapture,
        race: chat.race,
        contrat: {
          id: chat.contrat?.id ? chat.contrat.id : null,
          nom: chat.contrat ? chat.contrat.nom : null,
          prenom: chat.contrat ? chat.contrat.prenom : null,
          cout: chat.contrat ? chat.contrat.cout : null,
          paiement: chat.contrat ? chat.contrat.paiement : null,
          dateContrat: chat.contrat ? chat.contrat.dateContrat : null,
          adresseAdoptant: {
            id: chat.contrat?.adresseAdoptant ? chat.contrat?.adresseAdoptant.id : null,
            numero: chat.contrat?.adresseAdoptant ? chat.contrat?.adresseAdoptant.numero : null,
            rue: chat.contrat?.adresseAdoptant ? chat.contrat?.adresseAdoptant.rue : null,
            codePostale: chat.contrat?.adresseAdoptant ? chat.contrat?.adresseAdoptant.codePostale : null,
            ville: chat.contrat?.adresseAdoptant ? chat.contrat?.adresseAdoptant.ville : null,
          },
        },
      });
      this.possedeContrat = true;
      this.addValidatorContrat();
    }
    this.visites = chat.visites ? chat.visites : [];
  }

  protected createFromForm(): IChat {
    let chat = {
      ...new Chat(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      typeIdentification: this.editForm.get(['typeIdentification'])!.value,
      identification: this.editForm.get(['identification'])!.value,
      dateNaissance: this.editForm.get(['dateNaissance'])!.value,
      description: this.editForm.get(['description'])!.value,
      robe: this.editForm.get(['robe'])!.value,
      poil: this.editForm.get(['poil'])!.value,
      famille: this.editForm.get(['famille'])!.value,
      adresseCapture: this.editForm.get(['adresseCapture'])!.value,
      race: this.editForm.get(['race'])!.value,
      visites: this.visites,
    };
    if (this.possedeContrat === true) {
      chat = {
        ...chat,
        contrat: {
          id: this.editForm.get(['contrat', 'id'])!.value,
          nom: this.editForm.get(['contrat', 'nom'])!.value,
          prenom: this.editForm.get(['contrat', 'prenom'])!.value,
          paiement: this.editForm.get(['contrat', 'paiement'])!.value,
          cout: this.editForm.get(['contrat', 'cout'])!.value,
          dateContrat: this.editForm.get(['contrat', 'dateContrat'])!.value,
          adresseAdoptant: this.editForm.get(['contrat', 'adresseAdoptant'])!.value,
        },
      };
    }
    return chat;
  }
}
