import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IRaceChat, RaceChat } from '../race-chat.model';
import { RaceChatService } from '../service/race-chat.service';

@Component({
  selector: 'jhi-race-chat-update',
  templateUrl: './race-chat-update.component.html',
})
export class RaceChatUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    libelle: [],
  });

  constructor(protected raceChatService: RaceChatService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ raceChat }) => {
      this.updateForm(raceChat);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const raceChat = this.createFromForm();
    if (raceChat.id !== undefined) {
      this.subscribeToSaveResponse(this.raceChatService.update(raceChat));
    } else {
      this.subscribeToSaveResponse(this.raceChatService.create(raceChat));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRaceChat>>): void {
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

  protected updateForm(raceChat: IRaceChat): void {
    this.editForm.patchValue({
      id: raceChat.id,
      libelle: raceChat.libelle,
    });
  }

  protected createFromForm(): IRaceChat {
    return {
      ...new RaceChat(),
      id: this.editForm.get(['id'])!.value,
      libelle: this.editForm.get(['libelle'])!.value,
    };
  }
}
