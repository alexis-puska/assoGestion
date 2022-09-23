import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DonateurService } from '../service/donateur.service';
import { IDonateur, Donateur } from '../donateur.model';
import { IAdresse } from 'app/entities/adresse/adresse.model';
import { AdresseService } from 'app/entities/adresse/service/adresse.service';

import { DonateurUpdateComponent } from './donateur-update.component';

describe('Donateur Management Update Component', () => {
  let comp: DonateurUpdateComponent;
  let fixture: ComponentFixture<DonateurUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let donateurService: DonateurService;
  let adresseService: AdresseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DonateurUpdateComponent],
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
      .overrideTemplate(DonateurUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DonateurUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    donateurService = TestBed.inject(DonateurService);
    adresseService = TestBed.inject(AdresseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call adresse query and add missing value', () => {
      const donateur: IDonateur = { id: 456 };
      const adresse: IAdresse = { id: 81404 };
      donateur.adresse = adresse;

      const adresseCollection: IAdresse[] = [{ id: 97456 }];
      jest.spyOn(adresseService, 'query').mockReturnValue(of(new HttpResponse({ body: adresseCollection })));
      const expectedCollection: IAdresse[] = [adresse, ...adresseCollection];
      jest.spyOn(adresseService, 'addAdresseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ donateur });
      comp.ngOnInit();

      expect(adresseService.query).toHaveBeenCalled();
      expect(adresseService.addAdresseToCollectionIfMissing).toHaveBeenCalledWith(adresseCollection, adresse);
      expect(comp.adressesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const donateur: IDonateur = { id: 456 };
      const adresse: IAdresse = { id: 24065 };
      donateur.adresse = adresse;

      activatedRoute.data = of({ donateur });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(donateur));
      expect(comp.adressesCollection).toContain(adresse);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Donateur>>();
      const donateur = { id: 123 };
      jest.spyOn(donateurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ donateur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: donateur }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(donateurService.update).toHaveBeenCalledWith(donateur);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Donateur>>();
      const donateur = new Donateur();
      jest.spyOn(donateurService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ donateur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: donateur }));
      saveSubject.complete();

      // THEN
      expect(donateurService.create).toHaveBeenCalledWith(donateur);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Donateur>>();
      const donateur = { id: 123 };
      jest.spyOn(donateurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ donateur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(donateurService.update).toHaveBeenCalledWith(donateur);
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
