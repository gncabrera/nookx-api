import { ICurrency, NewCurrency } from './currency.model';

export const sampleWithRequiredData: ICurrency = {
  id: 30195,
};

export const sampleWithPartialData: ICurrency = {
  id: 255,
  name: 'Test Name',
  symbol: '¤',
};

export const sampleWithFullData: ICurrency = {
  id: 17828,
  code: 'TST',
  name: 'Test Currency',
  symbol: 'T',
};

export const sampleWithNewData: NewCurrency = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
