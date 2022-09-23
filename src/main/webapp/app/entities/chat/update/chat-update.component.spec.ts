import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ChatService } from '../service/chat.service';
import { IChat, Chat } from '../chat.model';
import { IContrat } from 'app/entities/contrat/contrat.model';
import { ContratService } from 'app/entities/contrat/service/contrat.service';
import { IFamilleAccueil } from 'app/entities/famille-accueil/famille-accueil.model';
import { FamilleAccueilService } from 'app/entities/famille-accueil/service/famille-accueil.service';
import { IPointCapture } from 'app/entities/point-capture/point-capture.model';
import { PointCaptureService } from 'app/entities/point-capture/service/point-capture.service';
import { IRaceChat } from 'app/entities/race-chat/race-chat.model';
import { RaceChatService } from 'app/entities/race-chat/service/race-chat.service';

import { ChatUpdateComponent } from './chat-update.component';

describe('Chat Management Update Component', () => {
  let comp: ChatUpdateComponent;
  let fixture: ComponentFixture<ChatUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let chatService: ChatService;
  let contratService: ContratService;
  let familleAccueilService: FamilleAccueilService;
  let pointCaptureService: PointCaptureService;
  let raceChatService: RaceChatService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ChatUpdateComponent],
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
      .overrideTemplate(ChatUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ChatUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    chatService = TestBed.inject(ChatService);
    contratService = TestBed.inject(ContratService);
    familleAccueilService = TestBed.inject(FamilleAccueilService);
    pointCaptureService = TestBed.inject(PointCaptureService);
    raceChatService = TestBed.inject(RaceChatService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call contrat query and add missing value', () => {
      const chat: IChat = { id: 456 };
      const contrat: IContrat = { id: 99133 };
      chat.contrat = contrat;

      const contratCollection: IContrat[] = [{ id: 87240 }];
      jest.spyOn(contratService, 'query').mockReturnValue(of(new HttpResponse({ body: contratCollection })));
      const expectedCollection: IContrat[] = [contrat, ...contratCollection];
      jest.spyOn(contratService, 'addContratToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ chat });
      comp.ngOnInit();

      expect(contratService.query).toHaveBeenCalled();
      expect(contratService.addContratToCollectionIfMissing).toHaveBeenCalledWith(contratCollection, contrat);
      expect(comp.contratsCollection).toEqual(expectedCollection);
    });

    it('Should call FamilleAccueil query and add missing value', () => {
      const chat: IChat = { id: 456 };
      const famille: IFamilleAccueil = { id: 78581 };
      chat.famille = famille;

      const familleAccueilCollection: IFamilleAccueil[] = [{ id: 46454 }];
      jest.spyOn(familleAccueilService, 'query').mockReturnValue(of(new HttpResponse({ body: familleAccueilCollection })));
      const additionalFamilleAccueils = [famille];
      const expectedCollection: IFamilleAccueil[] = [...additionalFamilleAccueils, ...familleAccueilCollection];
      jest.spyOn(familleAccueilService, 'addFamilleAccueilToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ chat });
      comp.ngOnInit();

      expect(familleAccueilService.query).toHaveBeenCalled();
      expect(familleAccueilService.addFamilleAccueilToCollectionIfMissing).toHaveBeenCalledWith(
        familleAccueilCollection,
        ...additionalFamilleAccueils
      );
      expect(comp.familleAccueilsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call PointCapture query and add missing value', () => {
      const chat: IChat = { id: 456 };
      const adresseCapture: IPointCapture = { id: 56421 };
      chat.adresseCapture = adresseCapture;

      const pointCaptureCollection: IPointCapture[] = [{ id: 77274 }];
      jest.spyOn(pointCaptureService, 'query').mockReturnValue(of(new HttpResponse({ body: pointCaptureCollection })));
      const additionalPointCaptures = [adresseCapture];
      const expectedCollection: IPointCapture[] = [...additionalPointCaptures, ...pointCaptureCollection];
      jest.spyOn(pointCaptureService, 'addPointCaptureToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ chat });
      comp.ngOnInit();

      expect(pointCaptureService.query).toHaveBeenCalled();
      expect(pointCaptureService.addPointCaptureToCollectionIfMissing).toHaveBeenCalledWith(
        pointCaptureCollection,
        ...additionalPointCaptures
      );
      expect(comp.pointCapturesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call RaceChat query and add missing value', () => {
      const chat: IChat = { id: 456 };
      const race: IRaceChat = { id: 25604 };
      chat.race = race;

      const raceChatCollection: IRaceChat[] = [{ id: 70725 }];
      jest.spyOn(raceChatService, 'query').mockReturnValue(of(new HttpResponse({ body: raceChatCollection })));
      const additionalRaceChats = [race];
      const expectedCollection: IRaceChat[] = [...additionalRaceChats, ...raceChatCollection];
      jest.spyOn(raceChatService, 'addRaceChatToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ chat });
      comp.ngOnInit();

      expect(raceChatService.query).toHaveBeenCalled();
      expect(raceChatService.addRaceChatToCollectionIfMissing).toHaveBeenCalledWith(raceChatCollection, ...additionalRaceChats);
      expect(comp.raceChatsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const chat: IChat = { id: 456 };
      const contrat: IContrat = { id: 27935 };
      chat.contrat = contrat;
      const famille: IFamilleAccueil = { id: 92480 };
      chat.famille = famille;
      const adresseCapture: IPointCapture = { id: 2758 };
      chat.adresseCapture = adresseCapture;
      const race: IRaceChat = { id: 49688 };
      chat.race = race;

      activatedRoute.data = of({ chat });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(chat));
      expect(comp.contratsCollection).toContain(contrat);
      expect(comp.familleAccueilsSharedCollection).toContain(famille);
      expect(comp.pointCapturesSharedCollection).toContain(adresseCapture);
      expect(comp.raceChatsSharedCollection).toContain(race);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Chat>>();
      const chat = { id: 123 };
      jest.spyOn(chatService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ chat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: chat }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(chatService.update).toHaveBeenCalledWith(chat);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Chat>>();
      const chat = new Chat();
      jest.spyOn(chatService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ chat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: chat }));
      saveSubject.complete();

      // THEN
      expect(chatService.create).toHaveBeenCalledWith(chat);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Chat>>();
      const chat = { id: 123 };
      jest.spyOn(chatService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ chat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(chatService.update).toHaveBeenCalledWith(chat);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackContratById', () => {
      it('Should return tracked Contrat primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackContratById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackFamilleAccueilById', () => {
      it('Should return tracked FamilleAccueil primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFamilleAccueilById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPointCaptureById', () => {
      it('Should return tracked PointCapture primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPointCaptureById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackRaceChatById', () => {
      it('Should return tracked RaceChat primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackRaceChatById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
