import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CliniqueVeterinaireService } from '../service/clinique-veterinaire.service';
import { ICliniqueVeterinaire, CliniqueVeterinaire } from '../clinique-veterinaire.model';
import { IAdresse } from 'app/entities/adresse/adresse.model';
import { AdresseService } from 'app/entities/adresse/service/adresse.service';

import { CliniqueVeterinaireUpdateComponent } from './clinique-veterinaire-update.component';

describe('CliniqueVeterinaire Management Update Component', () => {
  let comp: CliniqueVeterinaireUpdateComponent;
  let fixture: ComponentFixture<CliniqueVeterinaireUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cliniqueVeterinaireService: CliniqueVeterinaireService;
  let adresseService: AdresseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CliniqueVeterinaireUpdateComponent],
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
      .overrideTemplate(CliniqueVeterinaireUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CliniqueVeterinaireUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cliniqueVeterinaireService = TestBed.inject(CliniqueVeterinaireService);
    adresseService = TestBed.inject(AdresseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call adresse query and add missing value', () => {
      const cliniqueVeterinaire: ICliniqueVeterinaire = { id: 456 };
      const adresse: IAdresse = { id: 37279 };
      cliniqueVeterinaire.adresse = adresse;

      const adresseCollection: IAdresse[] = [{ id: 78222 }];
      jest.spyOn(adresseService, 'query').mockReturnValue(of(new HttpResponse({ body: adresseCollection })));
      const expectedCollection: IAdresse[] = [adresse, ...adresseCollection];
      jest.spyOn(adresseService, 'addAdresseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cliniqueVeterinaire });
      comp.ngOnInit();

      expect(adresseService.query).toHaveBeenCalled();
      expect(adresseService.addAdresseToCollectionIfMissing).toHaveBeenCalledWith(adresseCollection, adresse);
      expect(comp.adressesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const cliniqueVeterinaire: ICliniqueVeterinaire = { id: 456 };
      const adresse: IAdresse = { id: 28148 };
      cliniqueVeterinaire.adresse = adresse;

      activatedRoute.data = of({ cliniqueVeterinaire });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(cliniqueVeterinaire));
      expect(comp.adressesCollection).toContain(adresse);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CliniqueVeterinaire>>();
      const cliniqueVeterinaire = { id: 123 };
      jest.spyOn(cliniqueVeterinaireService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cliniqueVeterinaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cliniqueVeterinaire }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(cliniqueVeterinaireService.update).toHaveBeenCalledWith(cliniqueVeterinaire);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CliniqueVeterinaire>>();
      const cliniqueVeterinaire = new CliniqueVeterinaire();
      jest.spyOn(cliniqueVeterinaireService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cliniqueVeterinaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cliniqueVeterinaire }));
      saveSubject.complete();

      // THEN
      expect(cliniqueVeterinaireService.create).toHaveBeenCalledWith(cliniqueVeterinaire);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CliniqueVeterinaire>>();
      const cliniqueVeterinaire = { id: 123 };
      jest.spyOn(cliniqueVeterinaireService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cliniqueVeterinaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cliniqueVeterinaireService.update).toHaveBeenCalledWith(cliniqueVeterinaire);
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
