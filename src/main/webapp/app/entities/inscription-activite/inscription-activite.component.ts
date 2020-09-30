import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IInscriptionActivite } from 'app/shared/model/inscription-activite.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { InscriptionActiviteService } from './inscription-activite.service';
import { InscriptionActiviteDeleteDialogComponent } from './inscription-activite-delete-dialog.component';

@Component({
  selector: 'jhi-inscription-activite',
  templateUrl: './inscription-activite.component.html',
})
export class InscriptionActiviteComponent implements OnInit, OnDestroy {
  inscriptionActivites: IInscriptionActivite[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected inscriptionActiviteService: InscriptionActiviteService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.inscriptionActivites = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.inscriptionActiviteService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IInscriptionActivite[]>) => this.paginateInscriptionActivites(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.inscriptionActivites = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInInscriptionActivites();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IInscriptionActivite): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInInscriptionActivites(): void {
    this.eventSubscriber = this.eventManager.subscribe('inscriptionActiviteListModification', () => this.reset());
  }

  delete(inscriptionActivite: IInscriptionActivite): void {
    const modalRef = this.modalService.open(InscriptionActiviteDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.inscriptionActivite = inscriptionActivite;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateInscriptionActivites(data: IInscriptionActivite[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.inscriptionActivites.push(data[i]);
      }
    }
  }
}
