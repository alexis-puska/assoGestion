import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IConfigurationContrat, ConfigurationContrat } from '../configuration-contrat.model';

import { ConfigurationContratService } from './configuration-contrat.service';

describe('ConfigurationContrat Service', () => {
  let service: ConfigurationContratService;
  let httpMock: HttpTestingController;
  let elemDefault: IConfigurationContrat;
  let expectedResult: IConfigurationContrat | IConfigurationContrat[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ConfigurationContratService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      content: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a ConfigurationContrat', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ConfigurationContrat()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ConfigurationContrat', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          content: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ConfigurationContrat', () => {
      const patchObject = Object.assign({}, new ConfigurationContrat());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ConfigurationContrat', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          content: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a ConfigurationContrat', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addConfigurationContratToCollectionIfMissing', () => {
      it('should add a ConfigurationContrat to an empty array', () => {
        const configurationContrat: IConfigurationContrat = { id: 123 };
        expectedResult = service.addConfigurationContratToCollectionIfMissing([], configurationContrat);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(configurationContrat);
      });

      it('should not add a ConfigurationContrat to an array that contains it', () => {
        const configurationContrat: IConfigurationContrat = { id: 123 };
        const configurationContratCollection: IConfigurationContrat[] = [
          {
            ...configurationContrat,
          },
          { id: 456 },
        ];
        expectedResult = service.addConfigurationContratToCollectionIfMissing(configurationContratCollection, configurationContrat);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ConfigurationContrat to an array that doesn't contain it", () => {
        const configurationContrat: IConfigurationContrat = { id: 123 };
        const configurationContratCollection: IConfigurationContrat[] = [{ id: 456 }];
        expectedResult = service.addConfigurationContratToCollectionIfMissing(configurationContratCollection, configurationContrat);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(configurationContrat);
      });

      it('should add only unique ConfigurationContrat to an array', () => {
        const configurationContratArray: IConfigurationContrat[] = [{ id: 123 }, { id: 456 }, { id: 91419 }];
        const configurationContratCollection: IConfigurationContrat[] = [{ id: 123 }];
        expectedResult = service.addConfigurationContratToCollectionIfMissing(configurationContratCollection, ...configurationContratArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const configurationContrat: IConfigurationContrat = { id: 123 };
        const configurationContrat2: IConfigurationContrat = { id: 456 };
        expectedResult = service.addConfigurationContratToCollectionIfMissing([], configurationContrat, configurationContrat2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(configurationContrat);
        expect(expectedResult).toContain(configurationContrat2);
      });

      it('should accept null and undefined values', () => {
        const configurationContrat: IConfigurationContrat = { id: 123 };
        expectedResult = service.addConfigurationContratToCollectionIfMissing([], null, configurationContrat, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(configurationContrat);
      });

      it('should return initial array if no ConfigurationContrat is added', () => {
        const configurationContratCollection: IConfigurationContrat[] = [{ id: 123 }];
        expectedResult = service.addConfigurationContratToCollectionIfMissing(configurationContratCollection, undefined, null);
        expect(expectedResult).toEqual(configurationContratCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
