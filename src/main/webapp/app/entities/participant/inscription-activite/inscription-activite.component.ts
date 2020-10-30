import { Component, OnInit, Input } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IActivite } from 'app/shared/model/activite.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ActiviteService } from 'app/entities/activite/activite.service';
import { IParticipantActivite } from 'app/shared/model/participant-activite.model';
import { IEvenement } from 'app/shared/model/evenement.model';

@Component({
  selector: 'jhi-inscription-activite',
  templateUrl: './inscription-activite.component.html',
  styleUrls: ['./inscription-activite.component.scss'],
})
export class InscriptionActiviteComponent implements OnInit {
  @Input() participantActivites: IParticipantActivite[];
  @Input() activite: IActivite;
  @Input() evenement: IEvenement;

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
    this.participantActivites = [];
    this.evenement = {};
    this.activite = {};
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  // loadAll(): void {
  //   this.activiteService
  //     .query({
  //       page: this.page,
  //       size: this.itemsPerPage,
  //       sort: this.sort(),
  //     })
  //     .subscribe((res: HttpResponse<IActivite[]>) => this.paginateActivites(res.body, res.headers));
  // }

  reset(): void {
    this.page = 0;
    this.participantActivites = [];
  }

  loadPage(page: number): void {
    this.page = page;
    // this.loadAll();
  }

  ngOnInit(): void {}

  trackId(index: number, item: IParticipantActivite): number {
    return item.id!;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  saveActiviteParticipant(participant: IParticipantActivite, i: number): void {
    participant.activiteId = this.activite.id;
    participant.participantId = participant.participant?.id;
    participant.registered = !participant.registered;
    // tester ici si l'activité n'est pas saturée
    if (participant.activiteId != null && this.activiteService.testPlacedispo(participant.activiteId)) {
      this.activiteService.addParticipantActivite(participant).subscribe((res: HttpResponse<IParticipantActivite>) => {
        if (res && res.body) {
          this.participantActivites[i].registered = res.body.registered;
        }
      });
    }
  }
}
