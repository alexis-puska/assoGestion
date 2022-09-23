import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ActeVeterinaireService } from '../service/acte-veterinaire.service';
import { IActeVeterinaire, ActeVeterinaire } from '../acte-veterinaire.model';
import { IVisiteVeterinaire } from 'app/entities/visite-veterinaire/visite-veterinaire.model';
import { VisiteVeterinaireService } from 'app/entities/visite-veterinaire/service/visite-veterinaire.service';

import { ActeVeterinaireUpdateComponent } from './acte-veterinaire-update.component';

describe('ActeVeterinaire Management Update Component', () => {
  let comp: ActeVeterinaireUpdateComponent;
  let fixture: ComponentFixture<ActeVeterinaireUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let acteVeterinaireService: ActeVeterinaireService;
  let visiteVeterinaireService: VisiteVeterinaireService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ActeVeterinaireUpdateComponent],
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
      .overrideTemplate(ActeVeterinaireUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ActeVeterinaireUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    acteVeterinaireService = TestBed.inject(ActeVeterinaireService);
    visiteVeterinaireService = TestBed.inject(VisiteVeterinaireService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call VisiteVeterinaire query and add missing value', () => {
      const acteVeterinaire: IActeVeterinaire = { id: 456 };
      const visiteVeterinaire: IVisiteVeterinaire = { id: 78561 };
      acteVeterinaire.visiteVeterinaire = visiteVeterinaire;

      const visiteVeterinaireCollection: IVisiteVeterinaire[] = [{ id: 16929 }];
      jest.spyOn(visiteVeterinaireService, 'query').mockReturnValue(of(new HttpResponse({ body: visiteVeterinaireCollection })));
      const additionalVisiteVeterinaires = [visiteVeterinaire];
      const expectedCollection: IVisiteVeterinaire[] = [...additionalVisiteVeterinaires, ...visiteVeterinaireCollection];
      jest.spyOn(visiteVeterinaireService, 'addVisiteVeterinaireToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ acteVeterinaire });
      comp.ngOnInit();

      expect(visiteVeterinaireService.query).toHaveBeenCalled();
      expect(visiteVeterinaireService.addVisiteVeterinaireToCollectionIfMissing).toHaveBeenCalledWith(
        visiteVeterinaireCollection,
        ...additionalVisiteVeterinaires
      );
      expect(comp.visiteVeterinairesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const acteVeterinaire: IActeVeterinaire = { id: 456 };
      const visiteVeterinaire: IVisiteVeterinaire = { id: 5830 };
      acteVeterinaire.visiteVeterinaire = visiteVeterinaire;

      activatedRoute.data = of({ acteVeterinaire });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(acteVeterinaire));
      expect(comp.visiteVeterinairesSharedCollection).toContain(visiteVeterinaire);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ActeVeterinaire>>();
      const acteVeterinaire = { id: 123 };
      jest.spyOn(acteVeterinaireService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ acteVeterinaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: acteVeterinaire }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(acteVeterinaireService.update).toHaveBeenCalledWith(acteVeterinaire);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ActeVeterinaire>>();
      const acteVeterinaire = new ActeVeterinaire();
      jest.spyOn(acteVeterinaireService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ acteVeterinaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: acteVeterinaire }));
      saveSubject.complete();

      // THEN
      expect(acteVeterinaireService.create).toHaveBeenCalledWith(acteVeterinaire);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ActeVeterinaire>>();
      const acteVeterinaire = { id: 123 };
      jest.spyOn(acteVeterinaireService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ acteVeterinaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(acteVeterinaireService.update).toHaveBeenCalledWith(acteVeterinaire);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackVisiteVeterinaireById', () => {
      it('Should return tracked VisiteVeterinaire primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackVisiteVeterinaireById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
