import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';
import { of } from 'rxjs';

import { CurrencyService } from '../service/currency.service';

import { CurrencyDeleteDialog } from './currency-delete-dialog';

describe('Currency Management Delete Component', () => {
  let comp: CurrencyDeleteDialog;
  let fixture: ComponentFixture<CurrencyDeleteDialog>;
  let service: CurrencyService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [NgbActiveModal],
    });
    fixture = TestBed.createComponent(CurrencyDeleteDialog);
    comp = fixture.componentInstance;
    service = TestBed.inject(CurrencyService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('should call delete service on confirmDelete', () => {
      vitest.spyOn(service, 'delete').mockReturnValue(of(undefined));
      vitest.spyOn(mockActiveModal, 'close');

      comp.confirmDelete(123);

      expect(service.delete).toHaveBeenCalledWith(123);
      expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
    });

    it('should not call delete service on clear', () => {
      vitest.spyOn(service, 'delete');
      vitest.spyOn(mockActiveModal, 'close');
      vitest.spyOn(mockActiveModal, 'dismiss');

      comp.cancel();

      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
