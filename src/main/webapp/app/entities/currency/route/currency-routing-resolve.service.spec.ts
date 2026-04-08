import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpErrorResponse } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { ActivatedRoute, ActivatedRouteSnapshot, Router, convertToParamMap } from '@angular/router';

import { lastValueFrom, of, throwError } from 'rxjs';

import { CurrencyService } from '../service/currency.service';

import currencyResolve from './currency-routing-resolve.service';

describe('Currency routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: CurrencyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    vitest.spyOn(mockRouter, 'navigate');
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(CurrencyService);
  });

  describe('resolve', () => {
    it('should return ICurrency returned by find', async () => {
      service.find = vitest.fn(id => of({ id }));
      mockActivatedRouteSnapshot.params = { id: 123 };

      await new Promise<void>(resolve => {
        TestBed.runInInjectionContext(() => {
          currencyResolve(mockActivatedRouteSnapshot).subscribe({
            next(result) {
              expect(service.find).toHaveBeenCalledWith(123);
              expect(result).toEqual({ id: 123 });
              resolve();
            },
          });
        });
      });
    });

    it('should return null if id is not provided', async () => {
      service.find = vitest.fn();
      mockActivatedRouteSnapshot.params = {};

      await new Promise<void>(resolve => {
        TestBed.runInInjectionContext(() => {
          currencyResolve(mockActivatedRouteSnapshot).subscribe({
            next(result) {
              expect(service.find).not.toHaveBeenCalled();
              expect(result).toEqual(null);
              resolve();
            },
          });
        });
      });
    });

    it('should route to 404 page if data not found in server', async () => {
      vitest.spyOn(service, 'find').mockReturnValue(throwError(() => new HttpErrorResponse({ status: 404, statusText: 'Not Found' })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      await TestBed.runInInjectionContext(async () => {
        await expect(lastValueFrom(currencyResolve(mockActivatedRouteSnapshot))).rejects.toThrowError('no elements in sequence');
        expect(service.find).toHaveBeenCalledWith(123);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });

    it('should route to error page if server returns an error other than 404', async () => {
      vitest
        .spyOn(service, 'find')
        .mockReturnValue(throwError(() => new HttpErrorResponse({ status: 500, statusText: 'Internal Server Error' })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      await TestBed.runInInjectionContext(async () => {
        await expect(lastValueFrom(currencyResolve(mockActivatedRouteSnapshot))).rejects.toThrowError('no elements in sequence');
        expect(service.find).toHaveBeenCalledWith(123);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['error']);
      });
    });
  });
});
