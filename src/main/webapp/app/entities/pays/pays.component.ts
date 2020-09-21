import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPays } from 'app/shared/model/pays.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { PaysService } from './pays.service';
import { PaysDeleteDialogComponent } from './pays-delete-dialog.component';

@Component({
  selector: 'jhi-pays',
  templateUrl: './pays.component.html',
})
export class PaysComponent implements OnInit, OnDestroy {
  pays: IPays[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected paysService: PaysService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.pays = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.paysService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IPays[]>) => this.paginatePays(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.pays = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPays();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPays): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPays(): void {
    this.eventSubscriber = this.eventManager.subscribe('paysListModification', () => this.reset());
  }

  delete(pays: IPays): void {
    const modalRef = this.modalService.open(PaysDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.pays = pays;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginatePays(data: IPays[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.pays.push(data[i]);
      }
    }
  }
}
