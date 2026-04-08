import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ICurrency } from '../currency.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../currency.test-samples';

import { CurrencyService } from './currency.service';

const requireRestSample: ICurrency = {
  ...sampleWithRequiredData,
};

describe('Currency Service', () => {
  let service: CurrencyService;
  let httpMock: HttpTestingController;
  let expectedResult: ICurrency | ICurrency[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CurrencyService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Currency', () => {
      const currency = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(currency).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Currency', () => {
      const currency = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(currency).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Currency', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Currency', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Currency', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests.length).toBe(1);
    });

    describe('addCurrencyToCollectionIfMissing', () => {
      it('should add a Currency to an empty array', () => {
        const currency: ICurrency = sampleWithRequiredData;
        expectedResult = service.addCurrencyToCollectionIfMissing([], currency);
        expect(expectedResult).toEqual([currency]);
      });

      it('should not add a Currency to an array that contains it', () => {
        const currency: ICurrency = sampleWithRequiredData;
        const currencyCollection: ICurrency[] = [
          {
            ...currency,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCurrencyToCollectionIfMissing(currencyCollection, currency);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Currency to an array that doesn't contain it", () => {
        const currency: ICurrency = sampleWithRequiredData;
        const currencyCollection: ICurrency[] = [sampleWithPartialData];
        expectedResult = service.addCurrencyToCollectionIfMissing(currencyCollection, currency);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(currency);
      });

      it('should add only unique Currency to an array', () => {
        const currencyArray: ICurrency[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const currencyCollection: ICurrency[] = [sampleWithRequiredData];
        expectedResult = service.addCurrencyToCollectionIfMissing(currencyCollection, ...currencyArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const currency: ICurrency = sampleWithRequiredData;
        const currency2: ICurrency = sampleWithPartialData;
        expectedResult = service.addCurrencyToCollectionIfMissing([], currency, currency2);
        expect(expectedResult).toEqual([currency, currency2]);
      });

      it('should accept null and undefined values', () => {
        const currency: ICurrency = sampleWithRequiredData;
        expectedResult = service.addCurrencyToCollectionIfMissing([], null, currency, undefined);
        expect(expectedResult).toEqual([currency]);
      });

      it('should return initial array if no Currency is added', () => {
        const currencyCollection: ICurrency[] = [sampleWithRequiredData];
        expectedResult = service.addCurrencyToCollectionIfMissing(currencyCollection, undefined, null);
        expect(expectedResult).toEqual(currencyCollection);
      });
    });

    describe('compareCurrency', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCurrency(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 22355 };
        const entity2 = null;

        const compareResult1 = service.compareCurrency(entity1, entity2);
        const compareResult2 = service.compareCurrency(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 22355 };
        const entity2 = { id: 8466 };

        const compareResult1 = service.compareCurrency(entity1, entity2);
        const compareResult2 = service.compareCurrency(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 22355 };
        const entity2 = { id: 22355 };

        const compareResult1 = service.compareCurrency(entity1, entity2);
        const compareResult2 = service.compareCurrency(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
