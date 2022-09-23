import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CliniqueVeterinaireDetailComponent } from './clinique-veterinaire-detail.component';

describe('CliniqueVeterinaire Management Detail Component', () => {
  let comp: CliniqueVeterinaireDetailComponent;
  let fixture: ComponentFixture<CliniqueVeterinaireDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CliniqueVeterinaireDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ cliniqueVeterinaire: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CliniqueVeterinaireDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CliniqueVeterinaireDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load cliniqueVeterinaire on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.cliniqueVeterinaire).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
