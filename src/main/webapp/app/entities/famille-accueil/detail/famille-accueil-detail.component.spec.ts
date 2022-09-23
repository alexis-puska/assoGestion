import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FamilleAccueilDetailComponent } from './famille-accueil-detail.component';

describe('FamilleAccueil Management Detail Component', () => {
  let comp: FamilleAccueilDetailComponent;
  let fixture: ComponentFixture<FamilleAccueilDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FamilleAccueilDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ familleAccueil: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FamilleAccueilDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FamilleAccueilDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load familleAccueil on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.familleAccueil).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
