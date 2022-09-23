import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICliniqueVeterinaire, CliniqueVeterinaire } from '../clinique-veterinaire.model';

import { CliniqueVeterinaireService } from './clinique-veterinaire.service';

describe('CliniqueVeterinaire Service', () => {
  let service: CliniqueVeterinaireService;
  let httpMock: HttpTestingController;
  let elemDefault: ICliniqueVeterinaire;
  let expectedResult: ICliniqueVeterinaire | ICliniqueVeterinaire[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CliniqueVeterinaireService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nom: 'AAAAAAA',
      actif: false,
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

    it('should create a CliniqueVeterinaire', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CliniqueVeterinaire()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CliniqueVeterinaire', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nom: 'BBBBBB',
          actif: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CliniqueVeterinaire', () => {
      const patchObject = Object.assign(
        {
          nom: 'BBBBBB',
        },
        new CliniqueVeterinaire()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CliniqueVeterinaire', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nom: 'BBBBBB',
          actif: true,
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

    it('should delete a CliniqueVeterinaire', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCliniqueVeterinaireToCollectionIfMissing', () => {
      it('should add a CliniqueVeterinaire to an empty array', () => {
        const cliniqueVeterinaire: ICliniqueVeterinaire = { id: 123 };
        expectedResult = service.addCliniqueVeterinaireToCollectionIfMissing([], cliniqueVeterinaire);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cliniqueVeterinaire);
      });

      it('should not add a CliniqueVeterinaire to an array that contains it', () => {
        const cliniqueVeterinaire: ICliniqueVeterinaire = { id: 123 };
        const cliniqueVeterinaireCollection: ICliniqueVeterinaire[] = [
          {
            ...cliniqueVeterinaire,
          },
          { id: 456 },
        ];
        expectedResult = service.addCliniqueVeterinaireToCollectionIfMissing(cliniqueVeterinaireCollection, cliniqueVeterinaire);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CliniqueVeterinaire to an array that doesn't contain it", () => {
        const cliniqueVeterinaire: ICliniqueVeterinaire = { id: 123 };
        const cliniqueVeterinaireCollection: ICliniqueVeterinaire[] = [{ id: 456 }];
        expectedResult = service.addCliniqueVeterinaireToCollectionIfMissing(cliniqueVeterinaireCollection, cliniqueVeterinaire);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cliniqueVeterinaire);
      });

      it('should add only unique CliniqueVeterinaire to an array', () => {
        const cliniqueVeterinaireArray: ICliniqueVeterinaire[] = [{ id: 123 }, { id: 456 }, { id: 58250 }];
        const cliniqueVeterinaireCollection: ICliniqueVeterinaire[] = [{ id: 123 }];
        expectedResult = service.addCliniqueVeterinaireToCollectionIfMissing(cliniqueVeterinaireCollection, ...cliniqueVeterinaireArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cliniqueVeterinaire: ICliniqueVeterinaire = { id: 123 };
        const cliniqueVeterinaire2: ICliniqueVeterinaire = { id: 456 };
        expectedResult = service.addCliniqueVeterinaireToCollectionIfMissing([], cliniqueVeterinaire, cliniqueVeterinaire2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cliniqueVeterinaire);
        expect(expectedResult).toContain(cliniqueVeterinaire2);
      });

      it('should accept null and undefined values', () => {
        const cliniqueVeterinaire: ICliniqueVeterinaire = { id: 123 };
        expectedResult = service.addCliniqueVeterinaireToCollectionIfMissing([], null, cliniqueVeterinaire, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cliniqueVeterinaire);
      });

      it('should return initial array if no CliniqueVeterinaire is added', () => {
        const cliniqueVeterinaireCollection: ICliniqueVeterinaire[] = [{ id: 123 }];
        expectedResult = service.addCliniqueVeterinaireToCollectionIfMissing(cliniqueVeterinaireCollection, undefined, null);
        expect(expectedResult).toEqual(cliniqueVeterinaireCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
