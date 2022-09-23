import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { TypeLogementEnum } from 'app/entities/enumerations/type-logement-enum.model';
import { IFamilleAccueil, FamilleAccueil } from '../famille-accueil.model';

import { FamilleAccueilService } from './famille-accueil.service';

describe('FamilleAccueil Service', () => {
  let service: FamilleAccueilService;
  let httpMock: HttpTestingController;
  let elemDefault: IFamilleAccueil;
  let expectedResult: IFamilleAccueil | IFamilleAccueil[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FamilleAccueilService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nom: 'AAAAAAA',
      typeLogement: TypeLogementEnum.MAISON,
      nombrePiece: 0,
      nombreChat: 0,
      nombreChien: 0,
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

    it('should create a FamilleAccueil', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new FamilleAccueil()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FamilleAccueil', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nom: 'BBBBBB',
          typeLogement: 'BBBBBB',
          nombrePiece: 1,
          nombreChat: 1,
          nombreChien: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FamilleAccueil', () => {
      const patchObject = Object.assign(
        {
          nom: 'BBBBBB',
          nombrePiece: 1,
          nombreChien: 1,
        },
        new FamilleAccueil()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FamilleAccueil', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nom: 'BBBBBB',
          typeLogement: 'BBBBBB',
          nombrePiece: 1,
          nombreChat: 1,
          nombreChien: 1,
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

    it('should delete a FamilleAccueil', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFamilleAccueilToCollectionIfMissing', () => {
      it('should add a FamilleAccueil to an empty array', () => {
        const familleAccueil: IFamilleAccueil = { id: 123 };
        expectedResult = service.addFamilleAccueilToCollectionIfMissing([], familleAccueil);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(familleAccueil);
      });

      it('should not add a FamilleAccueil to an array that contains it', () => {
        const familleAccueil: IFamilleAccueil = { id: 123 };
        const familleAccueilCollection: IFamilleAccueil[] = [
          {
            ...familleAccueil,
          },
          { id: 456 },
        ];
        expectedResult = service.addFamilleAccueilToCollectionIfMissing(familleAccueilCollection, familleAccueil);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FamilleAccueil to an array that doesn't contain it", () => {
        const familleAccueil: IFamilleAccueil = { id: 123 };
        const familleAccueilCollection: IFamilleAccueil[] = [{ id: 456 }];
        expectedResult = service.addFamilleAccueilToCollectionIfMissing(familleAccueilCollection, familleAccueil);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(familleAccueil);
      });

      it('should add only unique FamilleAccueil to an array', () => {
        const familleAccueilArray: IFamilleAccueil[] = [{ id: 123 }, { id: 456 }, { id: 31037 }];
        const familleAccueilCollection: IFamilleAccueil[] = [{ id: 123 }];
        expectedResult = service.addFamilleAccueilToCollectionIfMissing(familleAccueilCollection, ...familleAccueilArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const familleAccueil: IFamilleAccueil = { id: 123 };
        const familleAccueil2: IFamilleAccueil = { id: 456 };
        expectedResult = service.addFamilleAccueilToCollectionIfMissing([], familleAccueil, familleAccueil2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(familleAccueil);
        expect(expectedResult).toContain(familleAccueil2);
      });

      it('should accept null and undefined values', () => {
        const familleAccueil: IFamilleAccueil = { id: 123 };
        expectedResult = service.addFamilleAccueilToCollectionIfMissing([], null, familleAccueil, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(familleAccueil);
      });

      it('should return initial array if no FamilleAccueil is added', () => {
        const familleAccueilCollection: IFamilleAccueil[] = [{ id: 123 }];
        expectedResult = service.addFamilleAccueilToCollectionIfMissing(familleAccueilCollection, undefined, null);
        expect(expectedResult).toEqual(familleAccueilCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
