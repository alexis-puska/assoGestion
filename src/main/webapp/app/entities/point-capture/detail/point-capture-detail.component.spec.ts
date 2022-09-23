import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PointCaptureDetailComponent } from './point-capture-detail.component';

describe('PointCapture Management Detail Component', () => {
  let comp: PointCaptureDetailComponent;
  let fixture: ComponentFixture<PointCaptureDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PointCaptureDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ pointCapture: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PointCaptureDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PointCaptureDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load pointCapture on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.pointCapture).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
