import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { VisiteVeterinaireService } from '../service/visite-veterinaire.service';
import { IVisiteVeterinaire, VisiteVeterinaire } from '../visite-veterinaire.model';
import { ICliniqueVeterinaire } from 'app/entities/clinique-veterinaire/clinique-veterinaire.model';
import { CliniqueVeterinaireService } from 'app/entities/clinique-veterinaire/service/clinique-veterinaire.service';
import { IChat } from 'app/entities/chat/chat.model';
import { ChatService } from 'app/entities/chat/service/chat.service';

import { VisiteVeterinaireUpdateComponent } from './visite-veterinaire-update.component';

describe('VisiteVeterinaire Management Update Component', () => {
  let comp: VisiteVeterinaireUpdateComponent;
  let fixture: ComponentFixture<VisiteVeterinaireUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let visiteVeterinaireService: VisiteVeterinaireService;
  let cliniqueVeterinaireService: CliniqueVeterinaireService;
  let chatService: ChatService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [VisiteVeterinaireUpdateComponent],
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
      .overrideTemplate(VisiteVeterinaireUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VisiteVeterinaireUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    visiteVeterinaireService = TestBed.inject(VisiteVeterinaireService);
    cliniqueVeterinaireService = TestBed.inject(CliniqueVeterinaireService);
    chatService = TestBed.inject(ChatService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call CliniqueVeterinaire query and add missing value', () => {
      const visiteVeterinaire: IVisiteVeterinaire = { id: 456 };
      const cliniqueVeterinaire: ICliniqueVeterinaire = { id: 71192 };
      visiteVeterinaire.cliniqueVeterinaire = cliniqueVeterinaire;

      const cliniqueVeterinaireCollection: ICliniqueVeterinaire[] = [{ id: 21822 }];
      jest.spyOn(cliniqueVeterinaireService, 'query').mockReturnValue(of(new HttpResponse({ body: cliniqueVeterinaireCollection })));
      const additionalCliniqueVeterinaires = [cliniqueVeterinaire];
      const expectedCollection: ICliniqueVeterinaire[] = [...additionalCliniqueVeterinaires, ...cliniqueVeterinaireCollection];
      jest.spyOn(cliniqueVeterinaireService, 'addCliniqueVeterinaireToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ visiteVeterinaire });
      comp.ngOnInit();

      expect(cliniqueVeterinaireService.query).toHaveBeenCalled();
      expect(cliniqueVeterinaireService.addCliniqueVeterinaireToCollectionIfMissing).toHaveBeenCalledWith(
        cliniqueVeterinaireCollection,
        ...additionalCliniqueVeterinaires
      );
      expect(comp.cliniqueVeterinairesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Chat query and add missing value', () => {
      const visiteVeterinaire: IVisiteVeterinaire = { id: 456 };
      const chat: IChat = { id: 64310 };
      visiteVeterinaire.chat = chat;

      const chatCollection: IChat[] = [{ id: 37844 }];
      jest.spyOn(chatService, 'query').mockReturnValue(of(new HttpResponse({ body: chatCollection })));
      const additionalChats = [chat];
      const expectedCollection: IChat[] = [...additionalChats, ...chatCollection];
      jest.spyOn(chatService, 'addChatToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ visiteVeterinaire });
      comp.ngOnInit();

      expect(chatService.query).toHaveBeenCalled();
      expect(chatService.addChatToCollectionIfMissing).toHaveBeenCalledWith(chatCollection, ...additionalChats);
      expect(comp.chatsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const visiteVeterinaire: IVisiteVeterinaire = { id: 456 };
      const cliniqueVeterinaire: ICliniqueVeterinaire = { id: 68307 };
      visiteVeterinaire.cliniqueVeterinaire = cliniqueVeterinaire;
      const chat: IChat = { id: 89125 };
      visiteVeterinaire.chat = chat;

      activatedRoute.data = of({ visiteVeterinaire });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(visiteVeterinaire));
      expect(comp.cliniqueVeterinairesSharedCollection).toContain(cliniqueVeterinaire);
      expect(comp.chatsSharedCollection).toContain(chat);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<VisiteVeterinaire>>();
      const visiteVeterinaire = { id: 123 };
      jest.spyOn(visiteVeterinaireService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ visiteVeterinaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: visiteVeterinaire }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(visiteVeterinaireService.update).toHaveBeenCalledWith(visiteVeterinaire);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<VisiteVeterinaire>>();
      const visiteVeterinaire = new VisiteVeterinaire();
      jest.spyOn(visiteVeterinaireService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ visiteVeterinaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: visiteVeterinaire }));
      saveSubject.complete();

      // THEN
      expect(visiteVeterinaireService.create).toHaveBeenCalledWith(visiteVeterinaire);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<VisiteVeterinaire>>();
      const visiteVeterinaire = { id: 123 };
      jest.spyOn(visiteVeterinaireService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ visiteVeterinaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(visiteVeterinaireService.update).toHaveBeenCalledWith(visiteVeterinaire);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCliniqueVeterinaireById', () => {
      it('Should return tracked CliniqueVeterinaire primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCliniqueVeterinaireById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackChatById', () => {
      it('Should return tracked Chat primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackChatById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
