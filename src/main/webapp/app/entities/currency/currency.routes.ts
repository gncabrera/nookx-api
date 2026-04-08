import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import CurrencyResolve from './route/currency-routing-resolve.service';

const currencyRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/currency').then(m => m.Currency),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/currency-detail').then(m => m.CurrencyDetail),
    resolve: {
      currency: CurrencyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/currency-update').then(m => m.CurrencyUpdate),
    resolve: {
      currency: CurrencyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/currency-update').then(m => m.CurrencyUpdate),
    resolve: {
      currency: CurrencyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default currencyRoute;
