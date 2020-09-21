import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IVille } from 'app/shared/model/ville.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { VilleService } from './ville.service';
import { VilleDeleteDialogComponent } from './ville-delete-dialog.component';

@Component({
  selector: 'jhi-ville',
  templateUrl: './ville.component.html',
})
export class VilleComponent implements OnInit, OnDestroy {
  villes: IVille[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected villeService: VilleService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.villes = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.villeService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IVille[]>) => this.paginateVilles(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.villes = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInVilles();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IVille): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInVilles(): void {
    this.eventSubscriber = this.eventManager.subscribe('villeListModification', () => this.reset());
  }

  delete(ville: IVille): void {
    const modalRef = this.modalService.open(VilleDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.ville = ville;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateVilles(data: IVille[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.villes.push(data[i]);
      }
    }
  }
}
