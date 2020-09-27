import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IInscription } from 'app/shared/model/inscription.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { InscriptionService } from './inscription.service';
import { InscriptionDeleteDialogComponent } from './inscription-delete-dialog.component';

@Component({
  selector: 'jhi-inscription',
  templateUrl: './inscription.component.html',
})
export class InscriptionComponent implements OnInit, OnDestroy {
  inscriptions: IInscription[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected inscriptionService: InscriptionService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.inscriptions = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.inscriptionService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IInscription[]>) => this.paginateInscriptions(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.inscriptions = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInInscriptions();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IInscription): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInInscriptions(): void {
    this.eventSubscriber = this.eventManager.subscribe('inscriptionListModification', () => this.reset());
  }

  delete(inscription: IInscription): void {
    const modalRef = this.modalService.open(InscriptionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.inscription = inscription;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateInscriptions(data: IInscription[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.inscriptions.push(data[i]);
      }
    }
  }
}
