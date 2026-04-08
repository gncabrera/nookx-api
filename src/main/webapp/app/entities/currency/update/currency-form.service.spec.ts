import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../currency.test-samples';

import { CurrencyFormService } from './currency-form.service';

describe('Currency Form Service', () => {
  let service: CurrencyFormService;

  beforeEach(() => {
    service = TestBed.inject(CurrencyFormService);
  });

  describe('Service methods', () => {
    describe('createCurrencyFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCurrencyFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            symbol: expect.any(Object),
          }),
        );
      });

      it('passing ICurrency should create a new form with FormGroup', () => {
        const formGroup = service.createCurrencyFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            symbol: expect.any(Object),
          }),
        );
      });
    });

    describe('getCurrency', () => {
      it('should return NewCurrency for default Currency initial value', () => {
        const formGroup = service.createCurrencyFormGroup(sampleWithNewData);

        const currency = service.getCurrency(formGroup);

        expect(currency).toMatchObject(sampleWithNewData);
      });

      it('should return NewCurrency for empty Currency initial value', () => {
        const formGroup = service.createCurrencyFormGroup();

        const currency = service.getCurrency(formGroup);

        expect(currency).toMatchObject({});
      });

      it('should return ICurrency', () => {
        const formGroup = service.createCurrencyFormGroup(sampleWithRequiredData);

        const currency = service.getCurrency(formGroup);

        expect(currency).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICurrency should not enable id FormControl', () => {
        const formGroup = service.createCurrencyFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCurrency should disable id FormControl', () => {
        const formGroup = service.createCurrencyFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
