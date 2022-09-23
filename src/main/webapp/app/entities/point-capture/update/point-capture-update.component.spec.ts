import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PointCaptureService } from '../service/point-capture.service';
import { IPointCapture, PointCapture } from '../point-capture.model';
import { IAdresse } from 'app/entities/adresse/adresse.model';
import { AdresseService } from 'app/entities/adresse/service/adresse.service';

import { PointCaptureUpdateComponent } from './point-capture-update.component';

describe('PointCapture Management Update Component', () => {
  let comp: PointCaptureUpdateComponent;
  let fixture: ComponentFixture<PointCaptureUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pointCaptureService: PointCaptureService;
  let adresseService: AdresseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PointCaptureUpdateComponent],
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
      .overrideTemplate(PointCaptureUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PointCaptureUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pointCaptureService = TestBed.inject(PointCaptureService);
    adresseService = TestBed.inject(AdresseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call adresseCapture query and add missing value', () => {
      const pointCapture: IPointCapture = { id: 456 };
      const adresseCapture: IAdresse = { id: 83892 };
      pointCapture.adresseCapture = adresseCapture;

      const adresseCaptureCollection: IAdresse[] = [{ id: 71034 }];
      jest.spyOn(adresseService, 'query').mockReturnValue(of(new HttpResponse({ body: adresseCaptureCollection })));
      const expectedCollection: IAdresse[] = [adresseCapture, ...adresseCaptureCollection];
      jest.spyOn(adresseService, 'addAdresseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pointCapture });
      comp.ngOnInit();

      expect(adresseService.query).toHaveBeenCalled();
      expect(adresseService.addAdresseToCollectionIfMissing).toHaveBeenCalledWith(adresseCaptureCollection, adresseCapture);
      expect(comp.adresseCapturesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const pointCapture: IPointCapture = { id: 456 };
      const adresseCapture: IAdresse = { id: 49078 };
      pointCapture.adresseCapture = adresseCapture;

      activatedRoute.data = of({ pointCapture });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(pointCapture));
      expect(comp.adresseCapturesCollection).toContain(adresseCapture);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PointCapture>>();
      const pointCapture = { id: 123 };
      jest.spyOn(pointCaptureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pointCapture });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pointCapture }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(pointCaptureService.update).toHaveBeenCalledWith(pointCapture);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PointCapture>>();
      const pointCapture = new PointCapture();
      jest.spyOn(pointCaptureService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pointCapture });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pointCapture }));
      saveSubject.complete();

      // THEN
      expect(pointCaptureService.create).toHaveBeenCalledWith(pointCapture);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PointCapture>>();
      const pointCapture = { id: 123 };
      jest.spyOn(pointCaptureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pointCapture });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pointCaptureService.update).toHaveBeenCalledWith(pointCapture);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackAdresseById', () => {
      it('Should return tracked Adresse primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAdresseById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
