import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ActiviteService } from 'app/entities/activite/activite.service';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { IActivite } from 'app/shared/model/activite.model';
import { IEvenement } from 'app/shared/model/evenement.model';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { Subscription } from 'rxjs';

@Component({
  selector: 'jhi-evenement-activite',
  templateUrl: './evenement-activite.component.html',
  styleUrls: ['./evenement-activite.component.scss'],
})
export class EvenementActiviteComponent implements OnInit, OnDestroy {
  @Input() activites: IActivite[];
  @Input() evenement: IEvenement;

  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected activiteService: ActiviteService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.activites = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
    this.evenement = {};
  }

  loadAll(): void {
    this.activiteService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IActivite[]>) => this.paginateActivites(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.activites = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    //this.loadAll();
  }

  ngOnInit(): void {
    //this.loadAll();
    this.registerChangeInActivites();
  }
  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IActivite): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInActivites(): void {
    this.eventSubscriber = this.eventManager.subscribe('activiteListModification', () => this.reset());
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateActivites(data: IActivite[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.activites.push(data[i]);
      }
    }
  }
}
