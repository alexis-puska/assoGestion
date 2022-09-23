import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DonateurDetailComponent } from './donateur-detail.component';

describe('Donateur Management Detail Component', () => {
  let comp: DonateurDetailComponent;
  let fixture: ComponentFixture<DonateurDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DonateurDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ donateur: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DonateurDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DonateurDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load donateur on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.donateur).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
