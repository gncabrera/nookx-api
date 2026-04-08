import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICurrency, NewCurrency } from '../currency.model';

type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

type CurrencyFormGroupInput = ICurrency | PartialWithRequiredKeyOf<NewCurrency>;

type CurrencyFormDefaults = Pick<NewCurrency, 'id'>;

type CurrencyFormGroupContent = {
  id: FormControl<ICurrency['id'] | NewCurrency['id']>;
  code: FormControl<ICurrency['code']>;
  name: FormControl<ICurrency['name']>;
  symbol: FormControl<ICurrency['symbol']>;
};

export type CurrencyFormGroup = FormGroup<CurrencyFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CurrencyFormService {
  createCurrencyFormGroup(currency?: CurrencyFormGroupInput): CurrencyFormGroup {
    const currencyRawValue = {
      ...this.getFormDefaults(),
      ...(currency ?? { id: null }),
    };
    return new FormGroup<CurrencyFormGroupContent>({
      id: new FormControl(
        { value: currencyRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(currencyRawValue.code),
      name: new FormControl(currencyRawValue.name),
      symbol: new FormControl(currencyRawValue.symbol),
    });
  }

  getCurrency(form: CurrencyFormGroup): ICurrency | NewCurrency {
    return form.getRawValue() as ICurrency | NewCurrency;
  }

  resetForm(form: CurrencyFormGroup, currency: CurrencyFormGroupInput): void {
    const currencyRawValue = { ...this.getFormDefaults(), ...currency };
    form.reset({
      ...currencyRawValue,
      id: { value: currencyRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): CurrencyFormDefaults {
    return {
      id: null,
    };
  }
}
