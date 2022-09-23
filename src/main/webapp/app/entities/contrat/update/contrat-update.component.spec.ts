import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ContratService } from '../service/contrat.service';
import { IContrat, Contrat } from '../contrat.model';
import { IAdresse } from 'app/entities/adresse/adresse.model';
import { AdresseService } from 'app/entities/adresse/service/adresse.service';

import { ContratUpdateComponent } from './contrat-update.component';

describe('Contrat Management Update Component', () => {
  let comp: ContratUpdateComponent;
  let fixture: ComponentFixture<ContratUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let contratService: ContratService;
  let adresseService: AdresseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ContratUpdateComponent],
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
      .overrideTemplate(ContratUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContratUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contratService = TestBed.inject(ContratService);
    adresseService = TestBed.inject(AdresseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call adresseAdoptant query and add missing value', () => {
      const contrat: IContrat = { id: 456 };
      const adresseAdoptant: IAdresse = { id: 98515 };
      contrat.adresseAdoptant = adresseAdoptant;

      const adresseAdoptantCollection: IAdresse[] = [{ id: 38395 }];
      jest.spyOn(adresseService, 'query').mockReturnValue(of(new HttpResponse({ body: adresseAdoptantCollection })));
      const expectedCollection: IAdresse[] = [adresseAdoptant, ...adresseAdoptantCollection];
      jest.spyOn(adresseService, 'addAdresseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contrat });
      comp.ngOnInit();

      expect(adresseService.query).toHaveBeenCalled();
      expect(adresseService.addAdresseToCollectionIfMissing).toHaveBeenCalledWith(adresseAdoptantCollection, adresseAdoptant);
      expect(comp.adresseAdoptantsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const contrat: IContrat = { id: 456 };
      const adresseAdoptant: IAdresse = { id: 46936 };
      contrat.adresseAdoptant = adresseAdoptant;

      activatedRoute.data = of({ contrat });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(contrat));
      expect(comp.adresseAdoptantsCollection).toContain(adresseAdoptant);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Contrat>>();
      const contrat = { id: 123 };
      jest.spyOn(contratService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contrat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contrat }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(contratService.update).toHaveBeenCalledWith(contrat);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Contrat>>();
      const contrat = new Contrat();
      jest.spyOn(contratService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contrat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contrat }));
      saveSubject.complete();

      // THEN
      expect(contratService.create).toHaveBeenCalledWith(contrat);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Contrat>>();
      const contrat = { id: 123 };
      jest.spyOn(contratService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contrat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contratService.update).toHaveBeenCalledWith(contrat);
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
