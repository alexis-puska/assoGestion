import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPointCapture, PointCapture } from '../point-capture.model';

import { PointCaptureService } from './point-capture.service';

describe('PointCapture Service', () => {
  let service: PointCaptureService;
  let httpMock: HttpTestingController;
  let elemDefault: IPointCapture;
  let expectedResult: IPointCapture | IPointCapture[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PointCaptureService);
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

    it('should create a PointCapture', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new PointCapture()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PointCapture', () => {
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

    it('should partial update a PointCapture', () => {
      const patchObject = Object.assign({}, new PointCapture());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PointCapture', () => {
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

    it('should delete a PointCapture', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPointCaptureToCollectionIfMissing', () => {
      it('should add a PointCapture to an empty array', () => {
        const pointCapture: IPointCapture = { id: 123 };
        expectedResult = service.addPointCaptureToCollectionIfMissing([], pointCapture);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pointCapture);
      });

      it('should not add a PointCapture to an array that contains it', () => {
        const pointCapture: IPointCapture = { id: 123 };
        const pointCaptureCollection: IPointCapture[] = [
          {
            ...pointCapture,
          },
          { id: 456 },
        ];
        expectedResult = service.addPointCaptureToCollectionIfMissing(pointCaptureCollection, pointCapture);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PointCapture to an array that doesn't contain it", () => {
        const pointCapture: IPointCapture = { id: 123 };
        const pointCaptureCollection: IPointCapture[] = [{ id: 456 }];
        expectedResult = service.addPointCaptureToCollectionIfMissing(pointCaptureCollection, pointCapture);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pointCapture);
      });

      it('should add only unique PointCapture to an array', () => {
        const pointCaptureArray: IPointCapture[] = [{ id: 123 }, { id: 456 }, { id: 86604 }];
        const pointCaptureCollection: IPointCapture[] = [{ id: 123 }];
        expectedResult = service.addPointCaptureToCollectionIfMissing(pointCaptureCollection, ...pointCaptureArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pointCapture: IPointCapture = { id: 123 };
        const pointCapture2: IPointCapture = { id: 456 };
        expectedResult = service.addPointCaptureToCollectionIfMissing([], pointCapture, pointCapture2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pointCapture);
        expect(expectedResult).toContain(pointCapture2);
      });

      it('should accept null and undefined values', () => {
        const pointCapture: IPointCapture = { id: 123 };
        expectedResult = service.addPointCaptureToCollectionIfMissing([], null, pointCapture, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pointCapture);
      });

      it('should return initial array if no PointCapture is added', () => {
        const pointCaptureCollection: IPointCapture[] = [{ id: 123 }];
        expectedResult = service.addPointCaptureToCollectionIfMissing(pointCaptureCollection, undefined, null);
        expect(expectedResult).toEqual(pointCaptureCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
