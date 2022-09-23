import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ConfigurationDonService } from '../service/configuration-don.service';
import { IConfigurationDon, ConfigurationDon } from '../configuration-don.model';
import { IAdresse } from 'app/entities/adresse/adresse.model';
import { AdresseService } from 'app/entities/adresse/service/adresse.service';

import { ConfigurationDonUpdateComponent } from './configuration-don-update.component';

describe('ConfigurationDon Management Update Component', () => {
  let comp: ConfigurationDonUpdateComponent;
  let fixture: ComponentFixture<ConfigurationDonUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let configurationDonService: ConfigurationDonService;
  let adresseService: AdresseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ConfigurationDonUpdateComponent],
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
      .overrideTemplate(ConfigurationDonUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConfigurationDonUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    configurationDonService = TestBed.inject(ConfigurationDonService);
    adresseService = TestBed.inject(AdresseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call adresse query and add missing value', () => {
      const configurationDon: IConfigurationDon = { id: 456 };
      const adresse: IAdresse = { id: 82769 };
      configurationDon.adresse = adresse;

      const adresseCollection: IAdresse[] = [{ id: 25932 }];
      jest.spyOn(adresseService, 'query').mockReturnValue(of(new HttpResponse({ body: adresseCollection })));
      const expectedCollection: IAdresse[] = [adresse, ...adresseCollection];
      jest.spyOn(adresseService, 'addAdresseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ configurationDon });
      comp.ngOnInit();

      expect(adresseService.query).toHaveBeenCalled();
      expect(adresseService.addAdresseToCollectionIfMissing).toHaveBeenCalledWith(adresseCollection, adresse);
      expect(comp.adressesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const configurationDon: IConfigurationDon = { id: 456 };
      const adresse: IAdresse = { id: 13858 };
      configurationDon.adresse = adresse;

      activatedRoute.data = of({ configurationDon });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(configurationDon));
      expect(comp.adressesCollection).toContain(adresse);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ConfigurationDon>>();
      const configurationDon = { id: 123 };
      jest.spyOn(configurationDonService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ configurationDon });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: configurationDon }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(configurationDonService.update).toHaveBeenCalledWith(configurationDon);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ConfigurationDon>>();
      const configurationDon = new ConfigurationDon();
      jest.spyOn(configurationDonService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ configurationDon });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: configurationDon }));
      saveSubject.complete();

      // THEN
      expect(configurationDonService.create).toHaveBeenCalledWith(configurationDon);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ConfigurationDon>>();
      const configurationDon = { id: 123 };
      jest.spyOn(configurationDonService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ configurationDon });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(configurationDonService.update).toHaveBeenCalledWith(configurationDon);
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
