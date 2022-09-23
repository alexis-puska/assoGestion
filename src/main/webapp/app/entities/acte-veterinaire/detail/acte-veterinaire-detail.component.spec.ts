import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ActeVeterinaireDetailComponent } from './acte-veterinaire-detail.component';

describe('ActeVeterinaire Management Detail Component', () => {
  let comp: ActeVeterinaireDetailComponent;
  let fixture: ComponentFixture<ActeVeterinaireDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ActeVeterinaireDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ acteVeterinaire: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ActeVeterinaireDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ActeVeterinaireDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load acteVeterinaire on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.acteVeterinaire).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
