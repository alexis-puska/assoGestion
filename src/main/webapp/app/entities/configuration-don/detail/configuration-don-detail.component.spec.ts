import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ConfigurationDonDetailComponent } from './configuration-don-detail.component';

describe('ConfigurationDon Management Detail Component', () => {
  let comp: ConfigurationDonDetailComponent;
  let fixture: ComponentFixture<ConfigurationDonDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ConfigurationDonDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ configurationDon: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ConfigurationDonDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ConfigurationDonDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load configurationDon on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.configurationDon).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
