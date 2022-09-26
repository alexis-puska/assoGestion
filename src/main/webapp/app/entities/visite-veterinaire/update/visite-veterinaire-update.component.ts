import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IVisiteVeterinaire, VisiteVeterinaire } from '../visite-veterinaire.model';
import { VisiteVeterinaireService } from '../service/visite-veterinaire.service';
import { ICliniqueVeterinaire } from 'app/entities/clinique-veterinaire/clinique-veterinaire.model';
import { CliniqueVeterinaireService } from 'app/entities/clinique-veterinaire/service/clinique-veterinaire.service';
import { IChat } from 'app/entities/chat/chat.model';
import { ChatService } from 'app/entities/chat/service/chat.service';

@Component({
  selector: 'jhi-visite-veterinaire-update',
  templateUrl: './visite-veterinaire-update.component.html',
})
export class VisiteVeterinaireUpdateComponent implements OnInit {
  isSaving = false;

  cliniqueVeterinairesSharedCollection: ICliniqueVeterinaire[] = [];
  chatsSharedCollection: IChat[] = [];

  editForm = this.fb.group({
    id: [],
    dateVisite: [],
    cliniqueVeterinaire: [],
    chat: [],
  });

  constructor(
    protected visiteVeterinaireService: VisiteVeterinaireService,
    protected cliniqueVeterinaireService: CliniqueVeterinaireService,
    protected chatService: ChatService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ visiteVeterinaire }) => {
      this.updateForm(visiteVeterinaire);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const visiteVeterinaire = this.createFromForm();
    if (visiteVeterinaire.id !== undefined) {
      this.subscribeToSaveResponse(this.visiteVeterinaireService.update(visiteVeterinaire));
    } else {
      this.subscribeToSaveResponse(this.visiteVeterinaireService.create(visiteVeterinaire));
    }
  }

  trackCliniqueVeterinaireById(index: number, item: ICliniqueVeterinaire): number {
    return item.id!;
  }

  trackChatById(index: number, item: IChat): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVisiteVeterinaire>>): void {
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

  protected updateForm(visiteVeterinaire: IVisiteVeterinaire): void {
    this.editForm.patchValue({
      id: visiteVeterinaire.id,
      dateVisite: visiteVeterinaire.dateVisite,
      cliniqueVeterinaire: visiteVeterinaire.cliniqueVeterinaire,
      chatId: visiteVeterinaire.chatId,
    });

    this.cliniqueVeterinairesSharedCollection = this.cliniqueVeterinaireService.addCliniqueVeterinaireToCollectionIfMissing(
      this.cliniqueVeterinairesSharedCollection,
      visiteVeterinaire.cliniqueVeterinaire
    );
    /* this.chatsSharedCollection = this.chatService.addChatToCollectionIfMissing(this.chatsSharedCollection, visiteVeterinaire.chatId); */
  }

  protected loadRelationshipsOptions(): void {
    this.cliniqueVeterinaireService
      .query()
      .pipe(map((res: HttpResponse<ICliniqueVeterinaire[]>) => res.body ?? []))
      .pipe(
        map((cliniqueVeterinaires: ICliniqueVeterinaire[]) =>
          this.cliniqueVeterinaireService.addCliniqueVeterinaireToCollectionIfMissing(
            cliniqueVeterinaires,
            this.editForm.get('cliniqueVeterinaire')!.value
          )
        )
      )
      .subscribe((cliniqueVeterinaires: ICliniqueVeterinaire[]) => (this.cliniqueVeterinairesSharedCollection = cliniqueVeterinaires));

    this.chatService
      .query()
      .pipe(map((res: HttpResponse<IChat[]>) => res.body ?? []))
      .pipe(map((chats: IChat[]) => this.chatService.addChatToCollectionIfMissing(chats, this.editForm.get('chat')!.value)))
      .subscribe((chats: IChat[]) => (this.chatsSharedCollection = chats));
  }

  protected createFromForm(): IVisiteVeterinaire {
    return {
      ...new VisiteVeterinaire(),
      id: this.editForm.get(['id'])!.value,
      dateVisite: this.editForm.get(['dateVisite'])!.value,
      cliniqueVeterinaire: this.editForm.get(['cliniqueVeterinaire'])!.value,
      chatId: this.editForm.get(['chat'])!.value,
    };
  }
}
