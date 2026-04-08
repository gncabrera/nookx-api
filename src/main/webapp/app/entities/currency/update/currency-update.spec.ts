import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { Subject, from, of } from 'rxjs';

import { ICurrency } from '../currency.model';
import { CurrencyService } from '../service/currency.service';

import { CurrencyFormService } from './currency-form.service';
import { CurrencyUpdate } from './currency-update';

describe('Currency Management Update Component', () => {
  let comp: CurrencyUpdate;
  let fixture: ComponentFixture<CurrencyUpdate>;
  let activatedRoute: ActivatedRoute;
  let currencyFormService: CurrencyFormService;
  let currencyService: CurrencyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(CurrencyUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    currencyFormService = TestBed.inject(CurrencyFormService);
    currencyService = TestBed.inject(CurrencyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const c: ICurrency = { id: 8466 };

      activatedRoute.data = of({ currency: c });
      comp.ngOnInit();

      expect(comp.currency).toEqual(c);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      const saveSubject = new Subject<ICurrency>();
      const c = { id: 22355 };
      vitest.spyOn(currencyFormService, 'getCurrency').mockReturnValue(c);
      vitest.spyOn(currencyService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ currency: c });
      comp.ngOnInit();

      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(c);
      saveSubject.complete();

      expect(currencyFormService.getCurrency).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(currencyService.update).toHaveBeenCalledWith(expect.objectContaining(c));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      const saveSubject = new Subject<ICurrency>();
      const c = { id: 22355 };
      vitest.spyOn(currencyFormService, 'getCurrency').mockReturnValue({ id: null });
      vitest.spyOn(currencyService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ currency: null });
      comp.ngOnInit();

      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(c);
      saveSubject.complete();

      expect(currencyFormService.getCurrency).toHaveBeenCalled();
      expect(currencyService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      const saveSubject = new Subject<ICurrency>();
      const c = { id: 22355 };
      vitest.spyOn(currencyService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ currency: c });
      comp.ngOnInit();

      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      expect(currencyService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
