import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VisiteVeterinaireDetailComponent } from './visite-veterinaire-detail.component';

describe('VisiteVeterinaire Management Detail Component', () => {
  let comp: VisiteVeterinaireDetailComponent;
  let fixture: ComponentFixture<VisiteVeterinaireDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [VisiteVeterinaireDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ visiteVeterinaire: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(VisiteVeterinaireDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(VisiteVeterinaireDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load visiteVeterinaire on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.visiteVeterinaire).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
