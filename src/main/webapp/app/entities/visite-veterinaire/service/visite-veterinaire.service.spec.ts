import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IVisiteVeterinaire, VisiteVeterinaire } from '../visite-veterinaire.model';

import { VisiteVeterinaireService } from './visite-veterinaire.service';

describe('VisiteVeterinaire Service', () => {
  let service: VisiteVeterinaireService;
  let httpMock: HttpTestingController;
  let elemDefault: IVisiteVeterinaire;
  let expectedResult: IVisiteVeterinaire | IVisiteVeterinaire[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(VisiteVeterinaireService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      dateVisite: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateVisite: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a VisiteVeterinaire', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dateVisite: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateVisite: currentDate,
        },
        returnedFromService
      );

      service.create(new VisiteVeterinaire()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a VisiteVeterinaire', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          dateVisite: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateVisite: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a VisiteVeterinaire', () => {
      const patchObject = Object.assign(
        {
          dateVisite: currentDate.format(DATE_FORMAT),
        },
        new VisiteVeterinaire()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateVisite: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of VisiteVeterinaire', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          dateVisite: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateVisite: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a VisiteVeterinaire', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addVisiteVeterinaireToCollectionIfMissing', () => {
      it('should add a VisiteVeterinaire to an empty array', () => {
        const visiteVeterinaire: IVisiteVeterinaire = { id: 123 };
        expectedResult = service.addVisiteVeterinaireToCollectionIfMissing([], visiteVeterinaire);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(visiteVeterinaire);
      });

      it('should not add a VisiteVeterinaire to an array that contains it', () => {
        const visiteVeterinaire: IVisiteVeterinaire = { id: 123 };
        const visiteVeterinaireCollection: IVisiteVeterinaire[] = [
          {
            ...visiteVeterinaire,
          },
          { id: 456 },
        ];
        expectedResult = service.addVisiteVeterinaireToCollectionIfMissing(visiteVeterinaireCollection, visiteVeterinaire);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a VisiteVeterinaire to an array that doesn't contain it", () => {
        const visiteVeterinaire: IVisiteVeterinaire = { id: 123 };
        const visiteVeterinaireCollection: IVisiteVeterinaire[] = [{ id: 456 }];
        expectedResult = service.addVisiteVeterinaireToCollectionIfMissing(visiteVeterinaireCollection, visiteVeterinaire);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(visiteVeterinaire);
      });

      it('should add only unique VisiteVeterinaire to an array', () => {
        const visiteVeterinaireArray: IVisiteVeterinaire[] = [{ id: 123 }, { id: 456 }, { id: 32694 }];
        const visiteVeterinaireCollection: IVisiteVeterinaire[] = [{ id: 123 }];
        expectedResult = service.addVisiteVeterinaireToCollectionIfMissing(visiteVeterinaireCollection, ...visiteVeterinaireArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const visiteVeterinaire: IVisiteVeterinaire = { id: 123 };
        const visiteVeterinaire2: IVisiteVeterinaire = { id: 456 };
        expectedResult = service.addVisiteVeterinaireToCollectionIfMissing([], visiteVeterinaire, visiteVeterinaire2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(visiteVeterinaire);
        expect(expectedResult).toContain(visiteVeterinaire2);
      });

      it('should accept null and undefined values', () => {
        const visiteVeterinaire: IVisiteVeterinaire = { id: 123 };
        expectedResult = service.addVisiteVeterinaireToCollectionIfMissing([], null, visiteVeterinaire, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(visiteVeterinaire);
      });

      it('should return initial array if no VisiteVeterinaire is added', () => {
        const visiteVeterinaireCollection: IVisiteVeterinaire[] = [{ id: 123 }];
        expectedResult = service.addVisiteVeterinaireToCollectionIfMissing(visiteVeterinaireCollection, undefined, null);
        expect(expectedResult).toEqual(visiteVeterinaireCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
