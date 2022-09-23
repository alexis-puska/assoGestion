import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PointNourrissageService } from '../service/point-nourrissage.service';
import { IPointNourrissage, PointNourrissage } from '../point-nourrissage.model';
import { IAdresse } from 'app/entities/adresse/adresse.model';
import { AdresseService } from 'app/entities/adresse/service/adresse.service';

import { PointNourrissageUpdateComponent } from './point-nourrissage-update.component';

describe('PointNourrissage Management Update Component', () => {
  let comp: PointNourrissageUpdateComponent;
  let fixture: ComponentFixture<PointNourrissageUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pointNourrissageService: PointNourrissageService;
  let adresseService: AdresseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PointNourrissageUpdateComponent],
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
      .overrideTemplate(PointNourrissageUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PointNourrissageUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pointNourrissageService = TestBed.inject(PointNourrissageService);
    adresseService = TestBed.inject(AdresseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call adresse query and add missing value', () => {
      const pointNourrissage: IPointNourrissage = { id: 456 };
      const adresse: IAdresse = { id: 89767 };
      pointNourrissage.adresse = adresse;

      const adresseCollection: IAdresse[] = [{ id: 38541 }];
      jest.spyOn(adresseService, 'query').mockReturnValue(of(new HttpResponse({ body: adresseCollection })));
      const expectedCollection: IAdresse[] = [adresse, ...adresseCollection];
      jest.spyOn(adresseService, 'addAdresseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pointNourrissage });
      comp.ngOnInit();

      expect(adresseService.query).toHaveBeenCalled();
      expect(adresseService.addAdresseToCollectionIfMissing).toHaveBeenCalledWith(adresseCollection, adresse);
      expect(comp.adressesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const pointNourrissage: IPointNourrissage = { id: 456 };
      const adresse: IAdresse = { id: 26060 };
      pointNourrissage.adresse = adresse;

      activatedRoute.data = of({ pointNourrissage });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(pointNourrissage));
      expect(comp.adressesCollection).toContain(adresse);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PointNourrissage>>();
      const pointNourrissage = { id: 123 };
      jest.spyOn(pointNourrissageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pointNourrissage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pointNourrissage }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(pointNourrissageService.update).toHaveBeenCalledWith(pointNourrissage);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PointNourrissage>>();
      const pointNourrissage = new PointNourrissage();
      jest.spyOn(pointNourrissageService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pointNourrissage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pointNourrissage }));
      saveSubject.complete();

      // THEN
      expect(pointNourrissageService.create).toHaveBeenCalledWith(pointNourrissage);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PointNourrissage>>();
      const pointNourrissage = { id: 123 };
      jest.spyOn(pointNourrissageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pointNourrissage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pointNourrissageService.update).toHaveBeenCalledWith(pointNourrissage);
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
