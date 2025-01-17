import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IParticipant } from 'app/shared/model/participant.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ParticipantService } from './participant.service';
import { ParticipantDeleteDialogComponent } from './participant-delete-dialog.component';
import {AccountService} from "../../core/auth/account.service";
import {Account} from "../../core/user/account.model";

@Component({
  selector: 'jhi-participant',
  templateUrl: './participant.component.html',
})
export class ParticipantComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  participants: IParticipant[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected participantService: ParticipantService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks,
    private accountService: AccountService
  ) {
    this.participants = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.participantService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IParticipant[]>) => this.paginateParticipants(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.participants = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInParticipants();
    this.accountService.identity().subscribe(account => (this.account = account));
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IParticipant): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInParticipants(): void {
    this.eventSubscriber = this.eventManager.subscribe('participantListModification', () => this.reset());
  }

  delete(participant: IParticipant): void {
    const modalRef = this.modalService.open(ParticipantDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.participant = participant;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateParticipants(data: IParticipant[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.participants.push(data[i]);
      }
    }
  }
}
