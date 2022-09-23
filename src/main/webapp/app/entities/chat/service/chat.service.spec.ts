import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { TypeIdentificationEnum } from 'app/entities/enumerations/type-identification-enum.model';
import { PoilEnum } from 'app/entities/enumerations/poil-enum.model';
import { IChat, Chat } from '../chat.model';

import { ChatService } from './chat.service';

describe('Chat Service', () => {
  let service: ChatService;
  let httpMock: HttpTestingController;
  let elemDefault: IChat;
  let expectedResult: IChat | IChat[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ChatService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      nom: 'AAAAAAA',
      typeIdentification: TypeIdentificationEnum.PUCE,
      identification: 'AAAAAAA',
      dateNaissance: currentDate,
      description: 'AAAAAAA',
      robe: 'AAAAAAA',
      poil: PoilEnum.SANS,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateNaissance: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Chat', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dateNaissance: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateNaissance: currentDate,
        },
        returnedFromService
      );

      service.create(new Chat()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Chat', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nom: 'BBBBBB',
          typeIdentification: 'BBBBBB',
          identification: 'BBBBBB',
          dateNaissance: currentDate.format(DATE_FORMAT),
          description: 'BBBBBB',
          robe: 'BBBBBB',
          poil: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateNaissance: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Chat', () => {
      const patchObject = Object.assign({}, new Chat());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateNaissance: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Chat', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nom: 'BBBBBB',
          typeIdentification: 'BBBBBB',
          identification: 'BBBBBB',
          dateNaissance: currentDate.format(DATE_FORMAT),
          description: 'BBBBBB',
          robe: 'BBBBBB',
          poil: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateNaissance: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Chat', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addChatToCollectionIfMissing', () => {
      it('should add a Chat to an empty array', () => {
        const chat: IChat = { id: 123 };
        expectedResult = service.addChatToCollectionIfMissing([], chat);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(chat);
      });

      it('should not add a Chat to an array that contains it', () => {
        const chat: IChat = { id: 123 };
        const chatCollection: IChat[] = [
          {
            ...chat,
          },
          { id: 456 },
        ];
        expectedResult = service.addChatToCollectionIfMissing(chatCollection, chat);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Chat to an array that doesn't contain it", () => {
        const chat: IChat = { id: 123 };
        const chatCollection: IChat[] = [{ id: 456 }];
        expectedResult = service.addChatToCollectionIfMissing(chatCollection, chat);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(chat);
      });

      it('should add only unique Chat to an array', () => {
        const chatArray: IChat[] = [{ id: 123 }, { id: 456 }, { id: 72724 }];
        const chatCollection: IChat[] = [{ id: 123 }];
        expectedResult = service.addChatToCollectionIfMissing(chatCollection, ...chatArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const chat: IChat = { id: 123 };
        const chat2: IChat = { id: 456 };
        expectedResult = service.addChatToCollectionIfMissing([], chat, chat2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(chat);
        expect(expectedResult).toContain(chat2);
      });

      it('should accept null and undefined values', () => {
        const chat: IChat = { id: 123 };
        expectedResult = service.addChatToCollectionIfMissing([], null, chat, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(chat);
      });

      it('should return initial array if no Chat is added', () => {
        const chatCollection: IChat[] = [{ id: 123 }];
        expectedResult = service.addChatToCollectionIfMissing(chatCollection, undefined, null);
        expect(expectedResult).toEqual(chatCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
