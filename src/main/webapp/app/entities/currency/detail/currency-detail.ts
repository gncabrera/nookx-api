import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
import { ICurrency } from '../currency.model';

@Component({
  selector: 'jhi-currency-detail',
  templateUrl: './currency-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, RouterLink],
})
export class CurrencyDetail {
  readonly currency = input<ICurrency | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
