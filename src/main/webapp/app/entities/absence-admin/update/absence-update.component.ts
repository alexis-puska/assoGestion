import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { IAbsence, Absence } from '../../absence/absence.model';
import { AbsenceService } from '../../absence/service/absence.service';
import { NgbDateParserFormatter, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { MomentDateFormatter } from 'app/shared/util/dateformat';

@Component({
  selector: 'jhi-absence-update',
  templateUrl: './absence-update.component.html',
  providers: [{ provide: NgbDateParserFormatter, useClass: MomentDateFormatter }],
})
export class AbsenceUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    motif: [null, [Validators.maxLength(512)]],
    start: [null, [Validators.required]],
    end: [null, [Validators.required]],
    user: [null, [Validators.required]],
  });

  constructor(protected absenceService: AbsenceService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ absence }) => {
      this.updateForm(absence);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const absence = this.createFromForm();
    if (absence.id !== undefined) {
      this.subscribeToSaveResponse(this.absenceService.updateAdmin(absence));
    } else {
      this.subscribeToSaveResponse(this.absenceService.createAdmin(absence));
    }
  }

  getMinDate(): NgbDateStruct {
    if (this.editForm.get('end')) {
      const date = this.editForm.get('start')?.value;
      if (date && dayjs.isDayjs(date) && date.isValid()) {
        return { year: date.year(), month: date.month() + 1, day: date.date() };
      }
    }
    return { year: 1972, month: 1, day: 1 };
  }

  getMinDateFormater(): string {
    if (this.editForm.get('end')) {
      const date = this.editForm.get('start')?.value;
      if (date && dayjs.isDayjs(date) && date.isValid()) {
        return date.format('DD/MM/YYYY');
      }
    }
    return '';
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAbsence>>): void {
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

  protected updateForm(absence: IAbsence): void {
    this.editForm.patchValue({
      id: absence.id,
      motif: absence.motif,
      start: absence.start,
      end: absence.end,
      user: absence.user,
    });
  }

  protected createFromForm(): IAbsence {
    return {
      ...new Absence(),
      id: this.editForm.get(['id'])!.value,
      motif: this.editForm.get(['motif'])!.value,
      start: this.editForm.get(['start'])!.value,
      end: this.editForm.get(['end'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
