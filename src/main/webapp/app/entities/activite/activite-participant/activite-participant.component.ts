import {Component, OnInit, OnDestroy, Input} from '@angular/core';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IActivite } from 'app/shared/model/activite.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import {IParticipant} from "../../../shared/model/participant.model";

@Component({
  selector: 'jhi-activite-participant',
  templateUrl: './activite-participant.component.html',
  styleUrls: ['./activite-participant.component.scss'],
})
export class ActiviteParticipantComponent implements OnInit, OnDestroy {
  @Input() activite: IActivite;
  @Input() participants: IParticipant[];

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
    this.activite = {};
    this.participants = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
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

  trackId(index: number, item: IParticipant): number {
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
