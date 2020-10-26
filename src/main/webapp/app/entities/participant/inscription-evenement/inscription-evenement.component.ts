import { HttpResponse } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { EvenementService } from 'app/entities/evenement/evenement.service';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { IParticipantEvenement } from 'app/shared/model/participant-evenement.model';
import { IParticipant } from 'app/shared/model/participant.model';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';

@Component({
  selector: 'jhi-inscription-evenement',
  templateUrl: './inscription-evenement.component.html',
  styleUrls: ['./inscription-evenement.component.scss'],
})
export class InscriptionEvenementComponent implements OnInit {
  @Input() participantEvenements: IParticipantEvenement[];
  @Input() participant: IParticipant;

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
    this.participant = {};
    this.participantEvenements = [];
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
    this.participantEvenements = [];
  }

  loadPage(page: number): void {
    this.page = page;
  }

  ngOnInit(): void {}
  trackId(index: number, item: IParticipantEvenement): number {
    return item.id!;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  saveInscriptionEvent(
    participantEvent: IParticipantEvenement,
    i: number,
    evenId?: number | undefined,
    partiId?: number | undefined
  ): void {
    participantEvent.evenementId = evenId;
    participantEvent.participantId = partiId;
    participantEvent.registered = !participantEvent.registered;
    this.evenementService.addParticipant(participantEvent).subscribe((res: HttpResponse<IParticipantEvenement>) => {
      if (res && res.body) {
        this.participantEvenements[i].registered = res.body.registered;
      }
    });
  }
}
