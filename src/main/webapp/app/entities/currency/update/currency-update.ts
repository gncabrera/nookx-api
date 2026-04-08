import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { AlertError } from 'app/shared/alert/alert-error';
import { ICurrency } from '../currency.model';
import { CurrencyService } from '../service/currency.service';

import { CurrencyFormGroup, CurrencyFormService } from './currency-form.service';

@Component({
  selector: 'jhi-currency-update',
  templateUrl: './currency-update.html',
  imports: [FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class CurrencyUpdate implements OnInit {
  readonly isSaving = signal(false);
  currency: ICurrency | null = null;

  protected currencyService = inject(CurrencyService);
  protected currencyFormService = inject(CurrencyFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CurrencyFormGroup = this.currencyFormService.createCurrencyFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ currency: c }) => {
      this.currency = c;
      if (c) {
        this.updateForm(c);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const c = this.currencyFormService.getCurrency(this.editForm);
    if (c.id === null) {
      this.subscribeToSaveResponse(this.currencyService.create(c));
    } else {
      this.subscribeToSaveResponse(this.currencyService.update(c));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ICurrency | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(c: ICurrency): void {
    this.currency = c;
    this.currencyFormService.resetForm(this.editForm, c);
  }
}
