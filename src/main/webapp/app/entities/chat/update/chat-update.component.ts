import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IChat, Chat } from '../chat.model';
import { ChatService } from '../service/chat.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IContrat } from 'app/entities/contrat/contrat.model';
import { ContratService } from 'app/entities/contrat/service/contrat.service';
import { IFamilleAccueil } from 'app/entities/famille-accueil/famille-accueil.model';
import { FamilleAccueilService } from 'app/entities/famille-accueil/service/famille-accueil.service';
import { IPointCapture } from 'app/entities/point-capture/point-capture.model';
import { PointCaptureService } from 'app/entities/point-capture/service/point-capture.service';
import { IRaceChat } from 'app/entities/race-chat/race-chat.model';
import { RaceChatService } from 'app/entities/race-chat/service/race-chat.service';
import { TypeIdentificationEnum } from 'app/entities/enumerations/type-identification-enum.model';
import { PoilEnum } from 'app/entities/enumerations/poil-enum.model';
import { MomentDateFormatter } from 'app/shared/util/dateformat';
import { NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-chat-update',
  templateUrl: './chat-update.component.html',
  providers: [{ provide: NgbDateParserFormatter, useClass: MomentDateFormatter }],
})
export class ChatUpdateComponent implements OnInit {
  isSaving = false;
  typeIdentificationEnumValues = Object.keys(TypeIdentificationEnum);
  poilEnumValues = Object.keys(PoilEnum);

  contratsCollection: IContrat[] = [];
  familleAccueilsSharedCollection: IFamilleAccueil[] = [];
  pointCapturesSharedCollection: IPointCapture[] = [];
  raceChatsSharedCollection: IRaceChat[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required]],
    typeIdentification: [],
    identification: [],
    dateNaissance: [null, [Validators.required]],
    description: [],
    robe: [null, [Validators.required]],
    poil: [null, [Validators.required]],
    contrat: [],
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

      this.loadRelationshipsOptions();
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
    if (chat.id !== undefined) {
      this.subscribeToSaveResponse(this.chatService.update(chat));
    } else {
      this.subscribeToSaveResponse(this.chatService.create(chat));
    }
  }

  trackContratById(index: number, item: IContrat): number {
    return item.id!;
  }

  trackFamilleAccueilById(index: number, item: IFamilleAccueil): number {
    return item.id!;
  }

  trackPointCaptureById(index: number, item: IPointCapture): number {
    return item.id!;
  }

  trackRaceChatById(index: number, item: IRaceChat): number {
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
      contrat: chat.contrat,
      famille: chat.famille,
      adresseCapture: chat.adresseCapture,
      race: chat.race,
    });

    this.contratsCollection = this.contratService.addContratToCollectionIfMissing(this.contratsCollection, chat.contrat);
    this.familleAccueilsSharedCollection = this.familleAccueilService.addFamilleAccueilToCollectionIfMissing(
      this.familleAccueilsSharedCollection,
      chat.famille
    );
    this.pointCapturesSharedCollection = this.pointCaptureService.addPointCaptureToCollectionIfMissing(
      this.pointCapturesSharedCollection,
      chat.adresseCapture
    );
    this.raceChatsSharedCollection = this.raceChatService.addRaceChatToCollectionIfMissing(this.raceChatsSharedCollection, chat.race);
  }

  protected loadRelationshipsOptions(): void {
    this.contratService
      .query({ filter: 'chat-is-null' })
      .pipe(map((res: HttpResponse<IContrat[]>) => res.body ?? []))
      .pipe(
        map((contrats: IContrat[]) => this.contratService.addContratToCollectionIfMissing(contrats, this.editForm.get('contrat')!.value))
      )
      .subscribe((contrats: IContrat[]) => (this.contratsCollection = contrats));

    this.familleAccueilService
      .query()
      .pipe(map((res: HttpResponse<IFamilleAccueil[]>) => res.body ?? []))
      .pipe(
        map((familleAccueils: IFamilleAccueil[]) =>
          this.familleAccueilService.addFamilleAccueilToCollectionIfMissing(familleAccueils, this.editForm.get('famille')!.value)
        )
      )
      .subscribe((familleAccueils: IFamilleAccueil[]) => (this.familleAccueilsSharedCollection = familleAccueils));

    this.pointCaptureService
      .query()
      .pipe(map((res: HttpResponse<IPointCapture[]>) => res.body ?? []))
      .pipe(
        map((pointCaptures: IPointCapture[]) =>
          this.pointCaptureService.addPointCaptureToCollectionIfMissing(pointCaptures, this.editForm.get('adresseCapture')!.value)
        )
      )
      .subscribe((pointCaptures: IPointCapture[]) => (this.pointCapturesSharedCollection = pointCaptures));

    this.raceChatService
      .query()
      .pipe(map((res: HttpResponse<IRaceChat[]>) => res.body ?? []))
      .pipe(
        map((raceChats: IRaceChat[]) => this.raceChatService.addRaceChatToCollectionIfMissing(raceChats, this.editForm.get('race')!.value))
      )
      .subscribe((raceChats: IRaceChat[]) => (this.raceChatsSharedCollection = raceChats));
  }

  protected createFromForm(): IChat {
    return {
      ...new Chat(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      typeIdentification: this.editForm.get(['typeIdentification'])!.value,
      identification: this.editForm.get(['identification'])!.value,
      dateNaissance: this.editForm.get(['dateNaissance'])!.value,
      description: this.editForm.get(['description'])!.value,
      robe: this.editForm.get(['robe'])!.value,
      poil: this.editForm.get(['poil'])!.value,
      contrat: this.editForm.get(['contrat'])!.value,
      famille: this.editForm.get(['famille'])!.value,
      adresseCapture: this.editForm.get(['adresseCapture'])!.value,
      race: this.editForm.get(['race'])!.value,
    };
  }
}
