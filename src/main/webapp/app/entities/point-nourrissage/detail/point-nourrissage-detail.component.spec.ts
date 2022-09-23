import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PointNourrissageDetailComponent } from './point-nourrissage-detail.component';

describe('PointNourrissage Management Detail Component', () => {
  let comp: PointNourrissageDetailComponent;
  let fixture: ComponentFixture<PointNourrissageDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PointNourrissageDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ pointNourrissage: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PointNourrissageDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PointNourrissageDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load pointNourrissage on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.pointNourrissage).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
