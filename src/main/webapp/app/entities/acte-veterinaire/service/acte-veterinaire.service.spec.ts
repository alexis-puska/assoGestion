import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IActeVeterinaire, ActeVeterinaire } from '../acte-veterinaire.model';

import { ActeVeterinaireService } from './acte-veterinaire.service';

describe('ActeVeterinaire Service', () => {
  let service: ActeVeterinaireService;
  let httpMock: HttpTestingController;
  let elemDefault: IActeVeterinaire;
  let expectedResult: IActeVeterinaire | IActeVeterinaire[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ActeVeterinaireService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      libelle: 'AAAAAAA',
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

    it('should create a ActeVeterinaire', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ActeVeterinaire()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ActeVeterinaire', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          libelle: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ActeVeterinaire', () => {
      const patchObject = Object.assign({}, new ActeVeterinaire());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ActeVeterinaire', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          libelle: 'BBBBBB',
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

    it('should delete a ActeVeterinaire', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addActeVeterinaireToCollectionIfMissing', () => {
      it('should add a ActeVeterinaire to an empty array', () => {
        const acteVeterinaire: IActeVeterinaire = { id: 123 };
        expectedResult = service.addActeVeterinaireToCollectionIfMissing([], acteVeterinaire);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(acteVeterinaire);
      });

      it('should not add a ActeVeterinaire to an array that contains it', () => {
        const acteVeterinaire: IActeVeterinaire = { id: 123 };
        const acteVeterinaireCollection: IActeVeterinaire[] = [
          {
            ...acteVeterinaire,
          },
          { id: 456 },
        ];
        expectedResult = service.addActeVeterinaireToCollectionIfMissing(acteVeterinaireCollection, acteVeterinaire);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ActeVeterinaire to an array that doesn't contain it", () => {
        const acteVeterinaire: IActeVeterinaire = { id: 123 };
        const acteVeterinaireCollection: IActeVeterinaire[] = [{ id: 456 }];
        expectedResult = service.addActeVeterinaireToCollectionIfMissing(acteVeterinaireCollection, acteVeterinaire);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(acteVeterinaire);
      });

      it('should add only unique ActeVeterinaire to an array', () => {
        const acteVeterinaireArray: IActeVeterinaire[] = [{ id: 123 }, { id: 456 }, { id: 13227 }];
        const acteVeterinaireCollection: IActeVeterinaire[] = [{ id: 123 }];
        expectedResult = service.addActeVeterinaireToCollectionIfMissing(acteVeterinaireCollection, ...acteVeterinaireArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const acteVeterinaire: IActeVeterinaire = { id: 123 };
        const acteVeterinaire2: IActeVeterinaire = { id: 456 };
        expectedResult = service.addActeVeterinaireToCollectionIfMissing([], acteVeterinaire, acteVeterinaire2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(acteVeterinaire);
        expect(expectedResult).toContain(acteVeterinaire2);
      });

      it('should accept null and undefined values', () => {
        const acteVeterinaire: IActeVeterinaire = { id: 123 };
        expectedResult = service.addActeVeterinaireToCollectionIfMissing([], null, acteVeterinaire, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(acteVeterinaire);
      });

      it('should return initial array if no ActeVeterinaire is added', () => {
        const acteVeterinaireCollection: IActeVeterinaire[] = [{ id: 123 }];
        expectedResult = service.addActeVeterinaireToCollectionIfMissing(acteVeterinaireCollection, undefined, null);
        expect(expectedResult).toEqual(acteVeterinaireCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
