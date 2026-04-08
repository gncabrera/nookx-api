import { MockInstance, afterEach, beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed, inject } from '@angular/core/testing';
import { ActivatedRoute, convertToParamMap } from '@angular/router';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faEye, faPencilAlt, faPlus, faSort, faSortDown, faSortUp, faSync, faTimes } from '@fortawesome/free-solid-svg-icons';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap/modal';
import { Subject, of } from 'rxjs';

import { sampleWithRequiredData } from '../currency.test-samples';
import { CurrencyService } from '../service/currency.service';

import { Currency } from './currency';

vitest.useFakeTimers();

describe('Currency Management Component', () => {
  let httpMock: HttpTestingController;
  let comp: Currency;
  let fixture: ComponentFixture<Currency>;
  let service: CurrencyService;
  let routerNavigateSpy: MockInstance;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              }),
            ),
            snapshot: {
              queryParams: {},
              queryParamMap: convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              }),
            },
          },
        },
      ],
    });

    fixture = TestBed.createComponent(Currency);
    comp = fixture.componentInstance;
    service = TestBed.inject(CurrencyService);
    routerNavigateSpy = vitest.spyOn(comp.router, 'navigate');

    const library = TestBed.inject(FaIconLibrary);
    library.addIcons(faEye, faPencilAlt, faPlus, faSort, faSortDown, faSortUp, faSync, faTimes);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    TestBed.resetTestingModule();
    httpMock.verify();
  });

  it('should call load all on init', async () => {
    TestBed.tick();
    const req = httpMock.expectOne({ method: 'GET' });
    req.flush([{ id: 22355 }], { headers: { link: '<http://localhost/api/foo?page=1&size=20>; rel="next"' } });
    await vitest.runAllTimersAsync();

    expect(comp.isLoading()).toEqual(false);
    expect(comp.currencies()[0]).toEqual(expect.objectContaining({ id: 22355 }));
  });

  describe('trackId', () => {
    it('should forward to currencyService', () => {
      const entity = { id: 22355 };
      vitest.spyOn(service, 'getCurrencyIdentifier');
      const id = comp.trackId(entity);
      expect(service.getCurrencyIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });

  it('should calculate the sort attribute for a non-id attribute', () => {
    comp.navigateToWithComponentValues({ predicate: 'non-existing-column', order: 'asc' });

    expect(routerNavigateSpy).toHaveBeenLastCalledWith(
      expect.anything(),
      expect.objectContaining({
        queryParams: expect.objectContaining({
          sort: ['non-existing-column,asc'],
        }),
      }),
    );
  });

  it('should calculate the sort attribute for an id', () => {
    TestBed.tick();
    httpMock.expectOne({ method: 'GET' });

    expect(service.currenciesParams()).toMatchObject(expect.objectContaining({ sort: ['id,desc'] }));
  });

  describe('delete', () => {
    let ngbModal: NgbModal;
    let deleteModalMock: any;

    beforeEach(() => {
      deleteModalMock = { componentInstance: {}, closed: new Subject() };
      ngbModal = (comp as any).modalService;
      vitest.spyOn(ngbModal, 'open').mockReturnValue(deleteModalMock);
    });

    it('on confirm should call load', inject([], () => {
      vitest.spyOn(comp, 'load');

      comp.delete(sampleWithRequiredData);
      deleteModalMock.closed.next('deleted');

      expect(ngbModal.open).toHaveBeenCalled();
      expect(comp.load).toHaveBeenCalled();
    }));

    it('on dismiss should call load', inject([], () => {
      vitest.spyOn(comp, 'load');

      comp.delete(sampleWithRequiredData);
      deleteModalMock.closed.next();

      expect(ngbModal.open).toHaveBeenCalled();
      expect(comp.load).not.toHaveBeenCalled();
    }));
  });
});
