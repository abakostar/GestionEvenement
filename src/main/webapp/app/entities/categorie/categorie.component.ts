import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICategorie } from 'app/shared/model/categorie.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { CategorieService } from './categorie.service';
import { CategorieDeleteDialogComponent } from './categorie-delete-dialog.component';

@Component({
  selector: 'jhi-categorie',
  templateUrl: './categorie.component.html',
})
export class CategorieComponent implements OnInit, OnDestroy {
  categories: ICategorie[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected categorieService: CategorieService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.categories = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.categorieService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<ICategorie[]>) => this.paginateCategories(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.categories = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInCategories();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ICategorie): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInCategories(): void {
    this.eventSubscriber = this.eventManager.subscribe('categorieListModification', () => this.reset());
  }

  delete(categorie: ICategorie): void {
    const modalRef = this.modalService.open(CategorieDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.categorie = categorie;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateCategories(data: ICategorie[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.categories.push(data[i]);
      }
    }
  }
}
