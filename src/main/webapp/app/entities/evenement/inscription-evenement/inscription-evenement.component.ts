import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { IEvenement } from 'app/shared/model/evenement.model';
import { IParticipantEvenement } from 'app/shared/model/participant-evenement.model';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { Subscription } from 'rxjs';
import { EvenementService } from '../evenement.service';

@Component({
  selector: 'jhi-inscription-evenement',
  templateUrl: './inscription-evenement.component.html',
  styleUrls: ['./inscription-evenement.component.scss'],
})
export class InscriptionEvenementComponent implements OnInit, OnDestroy {
  @Input() participants: IParticipantEvenement[];
  @Input() evenement: IEvenement;

  evenements: IEvenement[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected evenementService: EvenementService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.participants = [];
    this.evenements = [];
    this.evenement = {};
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.evenementService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IEvenement[]>) => this.paginateEvenements(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.evenements = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }
  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInEvenements();
  }
  trackId(index: number, item: IEvenement): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInEvenements(): void {
    this.eventSubscriber = this.eventManager.subscribe('evenementListModification', () => this.reset());
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateEvenements(data: IEvenement[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.evenements.push(data[i]);
      }
    }
  }
  save(participant: IParticipantEvenement): void {
    participant.evenementId = this.evenement.id;
    participant.participantId = participant.id;
    participant.registered = !participant.registered;
    this.evenementService.addParticipant(participant).subscribe((res: HttpResponse<IParticipantEvenement>) => {
      if (res && res.body) {
        this.participants[0].registered = res.body.registered;
      }
    });
  }
}
