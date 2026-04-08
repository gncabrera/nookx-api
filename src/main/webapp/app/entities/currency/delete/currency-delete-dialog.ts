import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { AlertError } from 'app/shared/alert/alert-error';
import { ICurrency } from '../currency.model';
import { CurrencyService } from '../service/currency.service';

@Component({
  templateUrl: './currency-delete-dialog.html',
  imports: [FormsModule, FontAwesomeModule, AlertError],
})
export class CurrencyDeleteDialog {
  currency?: ICurrency;

  protected readonly currencyService = inject(CurrencyService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.currencyService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
