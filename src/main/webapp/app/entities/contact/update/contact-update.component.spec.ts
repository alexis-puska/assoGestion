import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ContactService } from '../service/contact.service';
import { IContact, Contact } from '../contact.model';
import { IFamilleAccueil } from 'app/entities/famille-accueil/famille-accueil.model';
import { FamilleAccueilService } from 'app/entities/famille-accueil/service/famille-accueil.service';
import { IPointNourrissage } from 'app/entities/point-nourrissage/point-nourrissage.model';
import { PointNourrissageService } from 'app/entities/point-nourrissage/service/point-nourrissage.service';

import { ContactUpdateComponent } from './contact-update.component';

describe('Contact Management Update Component', () => {
  let comp: ContactUpdateComponent;
  let fixture: ComponentFixture<ContactUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let contactService: ContactService;
  let familleAccueilService: FamilleAccueilService;
  let pointNourrissageService: PointNourrissageService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ContactUpdateComponent],
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
      .overrideTemplate(ContactUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContactUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contactService = TestBed.inject(ContactService);
    familleAccueilService = TestBed.inject(FamilleAccueilService);
    pointNourrissageService = TestBed.inject(PointNourrissageService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call FamilleAccueil query and add missing value', () => {
      const contact: IContact = { id: 456 };
      const familleAccueil: IFamilleAccueil = { id: 36613 };
      contact.familleAccueil = familleAccueil;

      const familleAccueilCollection: IFamilleAccueil[] = [{ id: 82890 }];
      jest.spyOn(familleAccueilService, 'query').mockReturnValue(of(new HttpResponse({ body: familleAccueilCollection })));
      const additionalFamilleAccueils = [familleAccueil];
      const expectedCollection: IFamilleAccueil[] = [...additionalFamilleAccueils, ...familleAccueilCollection];
      jest.spyOn(familleAccueilService, 'addFamilleAccueilToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contact });
      comp.ngOnInit();

      expect(familleAccueilService.query).toHaveBeenCalled();
      expect(familleAccueilService.addFamilleAccueilToCollectionIfMissing).toHaveBeenCalledWith(
        familleAccueilCollection,
        ...additionalFamilleAccueils
      );
      expect(comp.familleAccueilsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call PointNourrissage query and add missing value', () => {
      const contact: IContact = { id: 456 };
      const pointNourrissage: IPointNourrissage = { id: 2395 };
      contact.pointNourrissage = pointNourrissage;

      const pointNourrissageCollection: IPointNourrissage[] = [{ id: 35486 }];
      jest.spyOn(pointNourrissageService, 'query').mockReturnValue(of(new HttpResponse({ body: pointNourrissageCollection })));
      const additionalPointNourrissages = [pointNourrissage];
      const expectedCollection: IPointNourrissage[] = [...additionalPointNourrissages, ...pointNourrissageCollection];
      jest.spyOn(pointNourrissageService, 'addPointNourrissageToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contact });
      comp.ngOnInit();

      expect(pointNourrissageService.query).toHaveBeenCalled();
      expect(pointNourrissageService.addPointNourrissageToCollectionIfMissing).toHaveBeenCalledWith(
        pointNourrissageCollection,
        ...additionalPointNourrissages
      );
      expect(comp.pointNourrissagesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const contact: IContact = { id: 456 };
      const familleAccueil: IFamilleAccueil = { id: 62652 };
      contact.familleAccueil = familleAccueil;
      const pointNourrissage: IPointNourrissage = { id: 44443 };
      contact.pointNourrissage = pointNourrissage;

      activatedRoute.data = of({ contact });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(contact));
      expect(comp.familleAccueilsSharedCollection).toContain(familleAccueil);
      expect(comp.pointNourrissagesSharedCollection).toContain(pointNourrissage);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Contact>>();
      const contact = { id: 123 };
      jest.spyOn(contactService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contact });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contact }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(contactService.update).toHaveBeenCalledWith(contact);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Contact>>();
      const contact = new Contact();
      jest.spyOn(contactService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contact });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contact }));
      saveSubject.complete();

      // THEN
      expect(contactService.create).toHaveBeenCalledWith(contact);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Contact>>();
      const contact = { id: 123 };
      jest.spyOn(contactService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contact });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contactService.update).toHaveBeenCalledWith(contact);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackFamilleAccueilById', () => {
      it('Should return tracked FamilleAccueil primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFamilleAccueilById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPointNourrissageById', () => {
      it('Should return tracked PointNourrissage primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPointNourrissageById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
