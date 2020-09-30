import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IInscriptionEvenement } from 'app/shared/model/inscription-evenement.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { InscriptionEvenementService } from './inscription-evenement.service';
import { InscriptionEvenementDeleteDialogComponent } from './inscription-evenement-delete-dialog.component';

@Component({
  selector: 'jhi-inscription-evenement',
  templateUrl: './inscription-evenement.component.html',
})
export class InscriptionEvenementComponent implements OnInit, OnDestroy {
  inscriptionEvenements: IInscriptionEvenement[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected inscriptionEvenementService: InscriptionEvenementService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.inscriptionEvenements = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.inscriptionEvenementService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IInscriptionEvenement[]>) => this.paginateInscriptionEvenements(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.inscriptionEvenements = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInInscriptionEvenements();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IInscriptionEvenement): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInInscriptionEvenements(): void {
    this.eventSubscriber = this.eventManager.subscribe('inscriptionEvenementListModification', () => this.reset());
  }

  delete(inscriptionEvenement: IInscriptionEvenement): void {
    const modalRef = this.modalService.open(InscriptionEvenementDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.inscriptionEvenement = inscriptionEvenement;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateInscriptionEvenements(data: IInscriptionEvenement[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.inscriptionEvenements.push(data[i]);
      }
    }
  }
}
