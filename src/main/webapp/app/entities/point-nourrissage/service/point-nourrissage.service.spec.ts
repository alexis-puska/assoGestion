import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPointNourrissage, PointNourrissage } from '../point-nourrissage.model';

import { PointNourrissageService } from './point-nourrissage.service';

describe('PointNourrissage Service', () => {
  let service: PointNourrissageService;
  let httpMock: HttpTestingController;
  let elemDefault: IPointNourrissage;
  let expectedResult: IPointNourrissage | IPointNourrissage[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PointNourrissageService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nom: 'AAAAAAA',
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

    it('should create a PointNourrissage', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new PointNourrissage()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PointNourrissage', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nom: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PointNourrissage', () => {
      const patchObject = Object.assign(
        {
          nom: 'BBBBBB',
        },
        new PointNourrissage()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PointNourrissage', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nom: 'BBBBBB',
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

    it('should delete a PointNourrissage', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPointNourrissageToCollectionIfMissing', () => {
      it('should add a PointNourrissage to an empty array', () => {
        const pointNourrissage: IPointNourrissage = { id: 123 };
        expectedResult = service.addPointNourrissageToCollectionIfMissing([], pointNourrissage);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pointNourrissage);
      });

      it('should not add a PointNourrissage to an array that contains it', () => {
        const pointNourrissage: IPointNourrissage = { id: 123 };
        const pointNourrissageCollection: IPointNourrissage[] = [
          {
            ...pointNourrissage,
          },
          { id: 456 },
        ];
        expectedResult = service.addPointNourrissageToCollectionIfMissing(pointNourrissageCollection, pointNourrissage);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PointNourrissage to an array that doesn't contain it", () => {
        const pointNourrissage: IPointNourrissage = { id: 123 };
        const pointNourrissageCollection: IPointNourrissage[] = [{ id: 456 }];
        expectedResult = service.addPointNourrissageToCollectionIfMissing(pointNourrissageCollection, pointNourrissage);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pointNourrissage);
      });

      it('should add only unique PointNourrissage to an array', () => {
        const pointNourrissageArray: IPointNourrissage[] = [{ id: 123 }, { id: 456 }, { id: 12695 }];
        const pointNourrissageCollection: IPointNourrissage[] = [{ id: 123 }];
        expectedResult = service.addPointNourrissageToCollectionIfMissing(pointNourrissageCollection, ...pointNourrissageArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pointNourrissage: IPointNourrissage = { id: 123 };
        const pointNourrissage2: IPointNourrissage = { id: 456 };
        expectedResult = service.addPointNourrissageToCollectionIfMissing([], pointNourrissage, pointNourrissage2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pointNourrissage);
        expect(expectedResult).toContain(pointNourrissage2);
      });

      it('should accept null and undefined values', () => {
        const pointNourrissage: IPointNourrissage = { id: 123 };
        expectedResult = service.addPointNourrissageToCollectionIfMissing([], null, pointNourrissage, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pointNourrissage);
      });

      it('should return initial array if no PointNourrissage is added', () => {
        const pointNourrissageCollection: IPointNourrissage[] = [{ id: 123 }];
        expectedResult = service.addPointNourrissageToCollectionIfMissing(pointNourrissageCollection, undefined, null);
        expect(expectedResult).toEqual(pointNourrissageCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
