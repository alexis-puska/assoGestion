import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IConfigurationDon, ConfigurationDon } from '../configuration-don.model';

import { ConfigurationDonService } from './configuration-don.service';

describe('ConfigurationDon Service', () => {
  let service: ConfigurationDonService;
  let httpMock: HttpTestingController;
  let elemDefault: IConfigurationDon;
  let expectedResult: IConfigurationDon | IConfigurationDon[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ConfigurationDonService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      denomination: 'AAAAAAA',
      objet: 'AAAAAAA',
      signataire: 'AAAAAAA',
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

    it('should create a ConfigurationDon', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ConfigurationDon()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ConfigurationDon', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          denomination: 'BBBBBB',
          objet: 'BBBBBB',
          signataire: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ConfigurationDon', () => {
      const patchObject = Object.assign(
        {
          denomination: 'BBBBBB',
          objet: 'BBBBBB',
        },
        new ConfigurationDon()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ConfigurationDon', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          denomination: 'BBBBBB',
          objet: 'BBBBBB',
          signataire: 'BBBBBB',
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

    it('should delete a ConfigurationDon', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addConfigurationDonToCollectionIfMissing', () => {
      it('should add a ConfigurationDon to an empty array', () => {
        const configurationDon: IConfigurationDon = { id: 123 };
        expectedResult = service.addConfigurationDonToCollectionIfMissing([], configurationDon);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(configurationDon);
      });

      it('should not add a ConfigurationDon to an array that contains it', () => {
        const configurationDon: IConfigurationDon = { id: 123 };
        const configurationDonCollection: IConfigurationDon[] = [
          {
            ...configurationDon,
          },
          { id: 456 },
        ];
        expectedResult = service.addConfigurationDonToCollectionIfMissing(configurationDonCollection, configurationDon);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ConfigurationDon to an array that doesn't contain it", () => {
        const configurationDon: IConfigurationDon = { id: 123 };
        const configurationDonCollection: IConfigurationDon[] = [{ id: 456 }];
        expectedResult = service.addConfigurationDonToCollectionIfMissing(configurationDonCollection, configurationDon);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(configurationDon);
      });

      it('should add only unique ConfigurationDon to an array', () => {
        const configurationDonArray: IConfigurationDon[] = [{ id: 123 }, { id: 456 }, { id: 76031 }];
        const configurationDonCollection: IConfigurationDon[] = [{ id: 123 }];
        expectedResult = service.addConfigurationDonToCollectionIfMissing(configurationDonCollection, ...configurationDonArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const configurationDon: IConfigurationDon = { id: 123 };
        const configurationDon2: IConfigurationDon = { id: 456 };
        expectedResult = service.addConfigurationDonToCollectionIfMissing([], configurationDon, configurationDon2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(configurationDon);
        expect(expectedResult).toContain(configurationDon2);
      });

      it('should accept null and undefined values', () => {
        const configurationDon: IConfigurationDon = { id: 123 };
        expectedResult = service.addConfigurationDonToCollectionIfMissing([], null, configurationDon, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(configurationDon);
      });

      it('should return initial array if no ConfigurationDon is added', () => {
        const configurationDonCollection: IConfigurationDon[] = [{ id: 123 }];
        expectedResult = service.addConfigurationDonToCollectionIfMissing(configurationDonCollection, undefined, null);
        expect(expectedResult).toEqual(configurationDonCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
