import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { FormeDonEnum } from 'app/entities/enumerations/forme-don-enum.model';
import { NatureDon } from 'app/entities/enumerations/nature-don.model';
import { NumeraireDonEnum } from 'app/entities/enumerations/numeraire-don-enum.model';
import { IDonateur, Donateur } from '../donateur.model';

import { DonateurService } from './donateur.service';

describe('Donateur Service', () => {
  let service: DonateurService;
  let httpMock: HttpTestingController;
  let elemDefault: IDonateur;
  let expectedResult: IDonateur | IDonateur[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DonateurService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nom: 'AAAAAAA',
      prenom: 'AAAAAAA',
      montant: 0,
      sommeTouteLettre: 'AAAAAAA',
      formeDon: FormeDonEnum.ACTE_AUTHENTIQUE,
      natureDon: NatureDon.NUMERAIRE,
      numeraireDon: NumeraireDonEnum.ESPECE,
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

    it('should create a Donateur', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Donateur()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Donateur', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nom: 'BBBBBB',
          prenom: 'BBBBBB',
          montant: 1,
          sommeTouteLettre: 'BBBBBB',
          formeDon: 'BBBBBB',
          natureDon: 'BBBBBB',
          numeraireDon: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Donateur', () => {
      const patchObject = Object.assign(
        {
          prenom: 'BBBBBB',
          montant: 1,
          sommeTouteLettre: 'BBBBBB',
          formeDon: 'BBBBBB',
          natureDon: 'BBBBBB',
        },
        new Donateur()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Donateur', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nom: 'BBBBBB',
          prenom: 'BBBBBB',
          montant: 1,
          sommeTouteLettre: 'BBBBBB',
          formeDon: 'BBBBBB',
          natureDon: 'BBBBBB',
          numeraireDon: 'BBBBBB',
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

    it('should delete a Donateur', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDonateurToCollectionIfMissing', () => {
      it('should add a Donateur to an empty array', () => {
        const donateur: IDonateur = { id: 123 };
        expectedResult = service.addDonateurToCollectionIfMissing([], donateur);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(donateur);
      });

      it('should not add a Donateur to an array that contains it', () => {
        const donateur: IDonateur = { id: 123 };
        const donateurCollection: IDonateur[] = [
          {
            ...donateur,
          },
          { id: 456 },
        ];
        expectedResult = service.addDonateurToCollectionIfMissing(donateurCollection, donateur);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Donateur to an array that doesn't contain it", () => {
        const donateur: IDonateur = { id: 123 };
        const donateurCollection: IDonateur[] = [{ id: 456 }];
        expectedResult = service.addDonateurToCollectionIfMissing(donateurCollection, donateur);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(donateur);
      });

      it('should add only unique Donateur to an array', () => {
        const donateurArray: IDonateur[] = [{ id: 123 }, { id: 456 }, { id: 85014 }];
        const donateurCollection: IDonateur[] = [{ id: 123 }];
        expectedResult = service.addDonateurToCollectionIfMissing(donateurCollection, ...donateurArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const donateur: IDonateur = { id: 123 };
        const donateur2: IDonateur = { id: 456 };
        expectedResult = service.addDonateurToCollectionIfMissing([], donateur, donateur2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(donateur);
        expect(expectedResult).toContain(donateur2);
      });

      it('should accept null and undefined values', () => {
        const donateur: IDonateur = { id: 123 };
        expectedResult = service.addDonateurToCollectionIfMissing([], null, donateur, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(donateur);
      });

      it('should return initial array if no Donateur is added', () => {
        const donateurCollection: IDonateur[] = [{ id: 123 }];
        expectedResult = service.addDonateurToCollectionIfMissing(donateurCollection, undefined, null);
        expect(expectedResult).toEqual(donateurCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
