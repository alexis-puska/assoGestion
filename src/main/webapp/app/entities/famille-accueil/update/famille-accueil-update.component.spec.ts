import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FamilleAccueilService } from '../service/famille-accueil.service';
import { IFamilleAccueil, FamilleAccueil } from '../famille-accueil.model';
import { IAdresse } from 'app/entities/adresse/adresse.model';
import { AdresseService } from 'app/entities/adresse/service/adresse.service';

import { FamilleAccueilUpdateComponent } from './famille-accueil-update.component';

describe('FamilleAccueil Management Update Component', () => {
  let comp: FamilleAccueilUpdateComponent;
  let fixture: ComponentFixture<FamilleAccueilUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let familleAccueilService: FamilleAccueilService;
  let adresseService: AdresseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FamilleAccueilUpdateComponent],
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
      .overrideTemplate(FamilleAccueilUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FamilleAccueilUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    familleAccueilService = TestBed.inject(FamilleAccueilService);
    adresseService = TestBed.inject(AdresseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call adresse query and add missing value', () => {
      const familleAccueil: IFamilleAccueil = { id: 456 };
      const adresse: IAdresse = { id: 70114 };
      familleAccueil.adresse = adresse;

      const adresseCollection: IAdresse[] = [{ id: 51642 }];
      jest.spyOn(adresseService, 'query').mockReturnValue(of(new HttpResponse({ body: adresseCollection })));
      const expectedCollection: IAdresse[] = [adresse, ...adresseCollection];
      jest.spyOn(adresseService, 'addAdresseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ familleAccueil });
      comp.ngOnInit();

      expect(adresseService.query).toHaveBeenCalled();
      expect(adresseService.addAdresseToCollectionIfMissing).toHaveBeenCalledWith(adresseCollection, adresse);
      expect(comp.adressesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const familleAccueil: IFamilleAccueil = { id: 456 };
      const adresse: IAdresse = { id: 66495 };
      familleAccueil.adresse = adresse;

      activatedRoute.data = of({ familleAccueil });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(familleAccueil));
      expect(comp.adressesCollection).toContain(adresse);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FamilleAccueil>>();
      const familleAccueil = { id: 123 };
      jest.spyOn(familleAccueilService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ familleAccueil });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: familleAccueil }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(familleAccueilService.update).toHaveBeenCalledWith(familleAccueil);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FamilleAccueil>>();
      const familleAccueil = new FamilleAccueil();
      jest.spyOn(familleAccueilService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ familleAccueil });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: familleAccueil }));
      saveSubject.complete();

      // THEN
      expect(familleAccueilService.create).toHaveBeenCalledWith(familleAccueil);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FamilleAccueil>>();
      const familleAccueil = { id: 123 };
      jest.spyOn(familleAccueilService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ familleAccueil });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(familleAccueilService.update).toHaveBeenCalledWith(familleAccueil);
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
