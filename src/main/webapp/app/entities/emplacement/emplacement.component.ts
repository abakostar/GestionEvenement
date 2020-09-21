import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEmplacement } from 'app/shared/model/emplacement.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { EmplacementService } from './emplacement.service';
import { EmplacementDeleteDialogComponent } from './emplacement-delete-dialog.component';

@Component({
  selector: 'jhi-emplacement',
  templateUrl: './emplacement.component.html',
})
export class EmplacementComponent implements OnInit, OnDestroy {
  emplacements: IEmplacement[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected emplacementService: EmplacementService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.emplacements = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.emplacementService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IEmplacement[]>) => this.paginateEmplacements(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.emplacements = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInEmplacements();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IEmplacement): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInEmplacements(): void {
    this.eventSubscriber = this.eventManager.subscribe('emplacementListModification', () => this.reset());
  }

  delete(emplacement: IEmplacement): void {
    const modalRef = this.modalService.open(EmplacementDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.emplacement = emplacement;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateEmplacements(data: IEmplacement[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.emplacements.push(data[i]);
      }
    }
  }
}
