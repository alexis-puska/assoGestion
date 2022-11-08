import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AbsenceService } from '../service/absence.service';
import { IAbsence, Absence } from '../absence.model';

import { AbsenceUpdateComponent } from './absence-update.component';

describe('Absence Management Update Component', () => {
  let comp: AbsenceUpdateComponent;
  let fixture: ComponentFixture<AbsenceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let absenceService: AbsenceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AbsenceUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AbsenceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AbsenceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    absenceService = TestBed.inject(AbsenceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const absence: IAbsence = { id: 456 };

      activatedRoute.data = of({ absence });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(absence));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Absence>>();
      const absence = { id: 123 };
      jest.spyOn(absenceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ absence });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: absence }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(absenceService.update).toHaveBeenCalledWith(absence);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Absence>>();
      const absence = new Absence();
      jest.spyOn(absenceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ absence });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: absence }));
      saveSubject.complete();

      // THEN
      expect(absenceService.create).toHaveBeenCalledWith(absence);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Absence>>();
      const absence = { id: 123 };
      jest.spyOn(absenceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ absence });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(absenceService.update).toHaveBeenCalledWith(absence);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
