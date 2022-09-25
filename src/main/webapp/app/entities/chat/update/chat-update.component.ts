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
    /*     contrat: {
          id: [],
          nom: [],
          prenom: [],
          cout: [],
          paiement: [],
          dateContrat: [],
          adresseAdoptant: {
            id: [],
            numero: [],
            rue: [],
            codePostale: [],
            ville: [],
          },
        }, */
    famille: [],
    adresseCapture: [],
    race: [],
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
    if (chat.contrat) {
      this.initContratControls();
      this.editForm.patchValue({
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
    }
    this.visites = chat.visites ? chat.visites : [];
  }

  protected initContratControls(): void {
    this.editForm.addControl(
      'contrat',
      this.fb.group({
        id: [],
        nom: [],
        prenom: [],
        cout: [],
        paiement: [],
        dateContrat: [],
        adresseAdoptant: {
          id: [],
          numero: [],
          rue: [],
          codePostale: [],
          ville: [],
        },
      })
    );
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
    if (this.editForm.get(['contrat'])) {
      chat = {
        ...chat,
        contrat: this.editForm.get(['contrat'])!.value,
      };
    }
    return chat;
  }
}
