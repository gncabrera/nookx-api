import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { ICurrency, NewCurrency } from '../currency.model';

export type PartialUpdateCurrency = Partial<ICurrency> & Pick<ICurrency, 'id'>;

@Injectable()
export class CurrenciesService {
  readonly currenciesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly currenciesResource = httpResource<ICurrency[]>(() => {
    const params = this.currenciesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  readonly currencies = computed(() => (this.currenciesResource.hasValue() ? this.currenciesResource.value() : []));
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/currencies');
}

@Injectable({ providedIn: 'root' })
export class CurrencyService extends CurrenciesService {
  protected readonly http = inject(HttpClient);

  create(currency: NewCurrency): Observable<ICurrency> {
    return this.http.post<ICurrency>(this.resourceUrl, currency);
  }

  update(currency: ICurrency): Observable<ICurrency> {
    return this.http.put<ICurrency>(`${this.resourceUrl}/${encodeURIComponent(this.getCurrencyIdentifier(currency))}`, currency);
  }

  partialUpdate(currency: PartialUpdateCurrency): Observable<ICurrency> {
    return this.http.patch<ICurrency>(`${this.resourceUrl}/${encodeURIComponent(this.getCurrencyIdentifier(currency))}`, currency);
  }

  find(id: number): Observable<ICurrency> {
    return this.http.get<ICurrency>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<ICurrency[]>> {
    const options = createRequestOption(req);
    return this.http.get<ICurrency[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getCurrencyIdentifier(currency: Pick<ICurrency, 'id'>): number {
    return currency.id;
  }

  compareCurrency(o1: Pick<ICurrency, 'id'> | null, o2: Pick<ICurrency, 'id'> | null): boolean {
    return o1 && o2 ? this.getCurrencyIdentifier(o1) === this.getCurrencyIdentifier(o2) : o1 === o2;
  }

  addCurrencyToCollectionIfMissing<Type extends Pick<ICurrency, 'id'>>(
    currencyCollection: Type[],
    ...currenciesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const currencies: Type[] = currenciesToCheck.filter(isPresent);
    if (currencies.length > 0) {
      const currencyCollectionIdentifiers = currencyCollection.map(currencyItem => this.getCurrencyIdentifier(currencyItem));
      const currenciesToAdd = currencies.filter(currencyItem => {
        const currencyIdentifier = this.getCurrencyIdentifier(currencyItem);
        if (currencyCollectionIdentifiers.includes(currencyIdentifier)) {
          return false;
        }
        currencyCollectionIdentifiers.push(currencyIdentifier);
        return true;
      });
      return [...currenciesToAdd, ...currencyCollection];
    }
    return currencyCollection;
  }
}
