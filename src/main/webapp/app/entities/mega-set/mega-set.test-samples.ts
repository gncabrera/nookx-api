import dayjs from 'dayjs/esm';

import { IMegaSet, NewMegaSet } from './mega-set.model';

export const sampleWithRequiredData: IMegaSet = {
  id: 11613,
  setNumber: 'sympathetically',
  nameEN: 'esteemed',
  descriptionEN: 'aboard',
};

export const sampleWithPartialData: IMegaSet = {
  id: 6787,
  setNumber: 'gulp bide',
  releaseDate: dayjs('2026-03-31'),
  nameEN: 'gee',
  descriptionEN: 'precious',
  descriptionES: 'whose',
  attributes: null,
  attributesContentType: null,
};

export const sampleWithFullData: IMegaSet = {
  id: 14614,
  setNumber: 'pfft extract but',
  releaseDate: dayjs('2026-03-31'),
  notes: 'trusting yum above',
  nameEN: 'for jubilantly rekindle',
  nameES: 'husband iridescence better',
  nameDE: 'pleasant violent fiercely',
  nameFR: 'oof disclosure transparency',
  descriptionEN: 'youthfully french',
  descriptionES: 'besides',
  descriptionDE: 'so geez quaver',
  descriptionFR: 'aha',
  attributes: null,
  attributesContentType: null,
};

export const sampleWithNewData: NewMegaSet = {
  setNumber: 'mostly prance defrag',
  nameEN: 'vast tedious austere',
  descriptionEN: 'quarrel garage bookcase',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
