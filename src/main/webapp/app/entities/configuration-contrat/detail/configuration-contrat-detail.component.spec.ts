import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ConfigurationContratDetailComponent } from './configuration-contrat-detail.component';

describe('ConfigurationContrat Management Detail Component', () => {
  let comp: ConfigurationContratDetailComponent;
  let fixture: ComponentFixture<ConfigurationContratDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ConfigurationContratDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ configurationContrat: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ConfigurationContratDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ConfigurationContratDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load configurationContrat on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.configurationContrat).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
