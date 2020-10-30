import { HttpResponse } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { IActivite } from 'app/shared/model/activite.model';
import { IParticipantActivite } from 'app/shared/model/participant-activite.model';
import { JhiParseLinks } from 'ng-jhipster';
import { ActiviteService } from '../activite.service';

@Component({
  selector: 'jhi-activite-participants',
  templateUrl: './activite-participants.component.html',
  styleUrls: ['./activite-participants.component.scss'],
})
export class ActiviteParticipantsComponent implements OnInit {
  @Input() participants: IParticipantActivite[];
  @Input() activite: IActivite;

  predicate: string;
  ascending: boolean;
  itemsPerPage: number;
  links: any;
  page: number;

  constructor(protected parseLinks: JhiParseLinks, protected activiteService: ActiviteService) {
    this.participants = [];
    this.predicate = 'id';
    this.ascending = true;
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.activite = {};
  }
  ngOnInit(): void {}
  loadPage(page: number): void {
    this.page = page;
  }

  trackId(index: number, item: IParticipantActivite): number {
    return item.id!;
  }

  reset(): void {
    this.page = 0;
    this.participants = [];
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
          this.participants[i].registered = res.body.registered;
        }
      });
    }
  }
}
