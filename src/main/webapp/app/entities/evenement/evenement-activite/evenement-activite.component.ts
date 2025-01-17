import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { IActivite } from 'app/shared/model/activite.model';
import { IEvenement } from 'app/shared/model/evenement.model';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';

@Component({
  selector: 'jhi-evenement-activite',
  templateUrl: './evenement-activite.component.html',
  styleUrls: ['./evenement-activite.component.scss'],
})
export class EvenementActiviteComponent implements OnInit, OnDestroy {
  @Input() activites: IActivite[];
  @Input() evenement: IEvenement;

  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
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

  reset(): void {
    this.page = 0;
  }

  loadPage(page: number): void {
    this.page = page;
  }

  ngOnInit(): void {
  }

  ngOnDestroy(): void {
  }

  trackId(index: number, item: IActivite): number {
    return item.id!;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }
}
