import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRaceChat, RaceChat } from '../race-chat.model';

import { RaceChatService } from './race-chat.service';

describe('RaceChat Service', () => {
  let service: RaceChatService;
  let httpMock: HttpTestingController;
  let elemDefault: IRaceChat;
  let expectedResult: IRaceChat | IRaceChat[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RaceChatService);
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

    it('should create a RaceChat', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new RaceChat()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RaceChat', () => {
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

    it('should partial update a RaceChat', () => {
      const patchObject = Object.assign({}, new RaceChat());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RaceChat', () => {
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

    it('should delete a RaceChat', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRaceChatToCollectionIfMissing', () => {
      it('should add a RaceChat to an empty array', () => {
        const raceChat: IRaceChat = { id: 123 };
        expectedResult = service.addRaceChatToCollectionIfMissing([], raceChat);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(raceChat);
      });

      it('should not add a RaceChat to an array that contains it', () => {
        const raceChat: IRaceChat = { id: 123 };
        const raceChatCollection: IRaceChat[] = [
          {
            ...raceChat,
          },
          { id: 456 },
        ];
        expectedResult = service.addRaceChatToCollectionIfMissing(raceChatCollection, raceChat);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RaceChat to an array that doesn't contain it", () => {
        const raceChat: IRaceChat = { id: 123 };
        const raceChatCollection: IRaceChat[] = [{ id: 456 }];
        expectedResult = service.addRaceChatToCollectionIfMissing(raceChatCollection, raceChat);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(raceChat);
      });

      it('should add only unique RaceChat to an array', () => {
        const raceChatArray: IRaceChat[] = [{ id: 123 }, { id: 456 }, { id: 89270 }];
        const raceChatCollection: IRaceChat[] = [{ id: 123 }];
        expectedResult = service.addRaceChatToCollectionIfMissing(raceChatCollection, ...raceChatArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const raceChat: IRaceChat = { id: 123 };
        const raceChat2: IRaceChat = { id: 456 };
        expectedResult = service.addRaceChatToCollectionIfMissing([], raceChat, raceChat2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(raceChat);
        expect(expectedResult).toContain(raceChat2);
      });

      it('should accept null and undefined values', () => {
        const raceChat: IRaceChat = { id: 123 };
        expectedResult = service.addRaceChatToCollectionIfMissing([], null, raceChat, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(raceChat);
      });

      it('should return initial array if no RaceChat is added', () => {
        const raceChatCollection: IRaceChat[] = [{ id: 123 }];
        expectedResult = service.addRaceChatToCollectionIfMissing(raceChatCollection, undefined, null);
        expect(expectedResult).toEqual(raceChatCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
