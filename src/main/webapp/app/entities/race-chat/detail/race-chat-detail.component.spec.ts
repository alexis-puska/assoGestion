import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RaceChatDetailComponent } from './race-chat-detail.component';

describe('RaceChat Management Detail Component', () => {
  let comp: RaceChatDetailComponent;
  let fixture: ComponentFixture<RaceChatDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RaceChatDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ raceChat: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RaceChatDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RaceChatDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load raceChat on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.raceChat).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
