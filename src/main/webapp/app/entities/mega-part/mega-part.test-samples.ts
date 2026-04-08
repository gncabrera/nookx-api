import dayjs from 'dayjs/esm';

import { IMegaPart, NewMegaPart } from './mega-part.model';

export const sampleWithRequiredData: IMegaPart = {
  id: 2382,
  partNumber: 'whoever while surface',
  nameEN: 'enhance',
};

export const sampleWithPartialData: IMegaPart = {
  id: 29075,
  partNumber: 'supposing until even',
  nameEN: 'pfft flustered',
  nameDE: 'coagulate',
  description: 'sweetly',
  notes: 'creamy',
};

export const sampleWithFullData: IMegaPart = {
  id: 17063,
  releaseDate: dayjs('2026-03-30'),
  partNumber: 'bowling',
  nameEN: 'cannon mechanically who',
  nameES: 'now',
  nameDE: 'apropos ugh',
  nameFR: 'metal orderly',
  description: 'alongside obediently aside',
  notes: 'gee unto',
  attributes: null,
  attributesContentType: null,
};

export const sampleWithNewData: NewMegaPart = {
  partNumber: 'parsnip',
  nameEN: 'prejudge interestingly tarragon',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
