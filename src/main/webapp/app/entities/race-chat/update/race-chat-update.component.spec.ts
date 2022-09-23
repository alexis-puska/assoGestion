import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RaceChatService } from '../service/race-chat.service';
import { IRaceChat, RaceChat } from '../race-chat.model';

import { RaceChatUpdateComponent } from './race-chat-update.component';

describe('RaceChat Management Update Component', () => {
  let comp: RaceChatUpdateComponent;
  let fixture: ComponentFixture<RaceChatUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let raceChatService: RaceChatService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RaceChatUpdateComponent],
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
      .overrideTemplate(RaceChatUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RaceChatUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    raceChatService = TestBed.inject(RaceChatService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const raceChat: IRaceChat = { id: 456 };

      activatedRoute.data = of({ raceChat });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(raceChat));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RaceChat>>();
      const raceChat = { id: 123 };
      jest.spyOn(raceChatService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ raceChat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: raceChat }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(raceChatService.update).toHaveBeenCalledWith(raceChat);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RaceChat>>();
      const raceChat = new RaceChat();
      jest.spyOn(raceChatService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ raceChat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: raceChat }));
      saveSubject.complete();

      // THEN
      expect(raceChatService.create).toHaveBeenCalledWith(raceChat);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RaceChat>>();
      const raceChat = { id: 123 };
      jest.spyOn(raceChatService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ raceChat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(raceChatService.update).toHaveBeenCalledWith(raceChat);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
