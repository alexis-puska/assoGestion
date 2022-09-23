import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ConfigurationContratService } from '../service/configuration-contrat.service';
import { IConfigurationContrat, ConfigurationContrat } from '../configuration-contrat.model';

import { ConfigurationContratUpdateComponent } from './configuration-contrat-update.component';

describe('ConfigurationContrat Management Update Component', () => {
  let comp: ConfigurationContratUpdateComponent;
  let fixture: ComponentFixture<ConfigurationContratUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let configurationContratService: ConfigurationContratService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ConfigurationContratUpdateComponent],
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
      .overrideTemplate(ConfigurationContratUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConfigurationContratUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    configurationContratService = TestBed.inject(ConfigurationContratService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const configurationContrat: IConfigurationContrat = { id: 456 };

      activatedRoute.data = of({ configurationContrat });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(configurationContrat));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ConfigurationContrat>>();
      const configurationContrat = { id: 123 };
      jest.spyOn(configurationContratService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ configurationContrat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: configurationContrat }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(configurationContratService.update).toHaveBeenCalledWith(configurationContrat);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ConfigurationContrat>>();
      const configurationContrat = new ConfigurationContrat();
      jest.spyOn(configurationContratService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ configurationContrat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: configurationContrat }));
      saveSubject.complete();

      // THEN
      expect(configurationContratService.create).toHaveBeenCalledWith(configurationContrat);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ConfigurationContrat>>();
      const configurationContrat = { id: 123 };
      jest.spyOn(configurationContratService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ configurationContrat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(configurationContratService.update).toHaveBeenCalledWith(configurationContrat);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
