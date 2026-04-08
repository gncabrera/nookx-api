import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'Authorities' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'user-management',
    data: { pageTitle: 'UserManagements' },
    loadChildren: () => import('./admin/user-management/user-management.routes'),
  },
  {
    path: 'mega-set-type',
    data: { pageTitle: 'MegaSetTypes' },
    loadChildren: () => import('./mega-set-type/mega-set-type.routes'),
  },
  {
    path: 'mega-set',
    data: { pageTitle: 'MegaSets' },
    loadChildren: () => import('./mega-set/mega-set.routes'),
  },
  {
    path: 'mega-set-part-count',
    data: { pageTitle: 'MegaSetPartCounts' },
    loadChildren: () => import('./mega-set-part-count/mega-set-part-count.routes'),
  },
  {
    path: 'mega-part-type',
    data: { pageTitle: 'MegaPartTypes' },
    loadChildren: () => import('./mega-part-type/mega-part-type.routes'),
  },
  {
    path: 'mega-part',
    data: { pageTitle: 'MegaParts' },
    loadChildren: () => import('./mega-part/mega-part.routes'),
  },
  {
    path: 'mega-part-sub-part-count',
    data: { pageTitle: 'MegaPartSubPartCounts' },
    loadChildren: () => import('./mega-part-sub-part-count/mega-part-sub-part-count.routes'),
  },
  {
    path: 'mega-asset',
    data: { pageTitle: 'MegaAssets' },
    loadChildren: () => import('./mega-asset/mega-asset.routes'),
  },
  {
    path: 'part-sub-category',
    data: { pageTitle: 'PartSubCategories' },
    loadChildren: () => import('./part-sub-category/part-sub-category.routes'),
  },
  {
    path: 'part-category',
    data: { pageTitle: 'PartCategories' },
    loadChildren: () => import('./part-category/part-category.routes'),
  },
  {
    path: 'mega-attribute',
    data: { pageTitle: 'MegaAttributes' },
    loadChildren: () => import('./mega-attribute/mega-attribute.routes'),
  },
  {
    path: 'mega-attribute-option',
    data: { pageTitle: 'MegaAttributeOptions' },
    loadChildren: () => import('./mega-attribute-option/mega-attribute-option.routes'),
  },
  {
    path: 'profile',
    data: { pageTitle: 'Profiles' },
    loadChildren: () => import('./profile/profile.routes'),
  },
  {
    path: 'following-profile',
    data: { pageTitle: 'FollowingProfiles' },
    loadChildren: () => import('./following-profile/following-profile.routes'),
  },
  {
    path: 'blocked-profile',
    data: { pageTitle: 'BlockedProfiles' },
    loadChildren: () => import('./blocked-profile/blocked-profile.routes'),
  },
  {
    path: 'profile-collection',
    data: { pageTitle: 'ProfileCollections' },
    loadChildren: () => import('./profile-collection/profile-collection.routes'),
  },
  {
    path: 'profile-collection-set',
    data: { pageTitle: 'ProfileCollectionSets' },
    loadChildren: () => import('./profile-collection-set/profile-collection-set.routes'),
  },
  {
    path: 'profile-request',
    data: { pageTitle: 'ProfileRequests' },
    loadChildren: () => import('./profile-request/profile-request.routes'),
  },
  {
    path: 'profile-request-type',
    data: { pageTitle: 'ProfileRequestTypes' },
    loadChildren: () => import('./profile-request-type/profile-request-type.routes'),
  },
  {
    path: 'environment-variable',
    data: { pageTitle: 'EnvironmentVariables' },
    loadChildren: () => import('./environment-variable/environment-variable.routes'),
  },
  {
    path: 'currency',
    data: { pageTitle: 'Currencies' },
    loadChildren: () => import('./currency/currency.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
