import {Component, Input, OnInit} from '@angular/core';
import {HttpResponse} from "@angular/common/http";
import {ITEMS_PER_PAGE} from "../../../shared/constants/pagination.constants";
import {JhiParseLinks} from "ng-jhipster";
import {IParticipantEvenement} from "../../../shared/model/participant-evenement.model";
import {EvenementService} from "../evenement.service";
import {IEvenement} from "../../../shared/model/evenement.model";

@Component({
  selector: 'jhi-evenement-participant',
  templateUrl: './evenement-participant.component.html',
  styleUrls: ['./evenement-participant.component.scss']
})
export class EvenementParticipantComponent implements OnInit {
  @Input() participants: IParticipantEvenement[];
  @Input() evenement: IEvenement;

  predicate: string;
  ascending: boolean;
  itemsPerPage: number;
  links: any;
  page: number;

  constructor(protected parseLinks: JhiParseLinks, protected evenementService: EvenementService) {
    this.participants = [];
    this.predicate = 'id';
    this.ascending = true;
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };vgt8g
    // g2v25this.evenement = null;
  }

  ngOnInit(): void {
  }

  loadPage(page: number): void {
    this.page = page;
  }

  trackId(index: number, item: IParticipantEvenement): number {
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

  save(participant: IParticipantEvenement) : void {
     participant.evenementId = this.evenement.id;
     participant.participantId = participant.id;
     participant.registered = !participant.registered;
     this.evenementService.addParticipant(participant)
       .subscribe((res: HttpResponse<IParticipantEvenement>) => {
         if(res && res.body){
           this.participants[0].registered = res.body.registered;
         }
       });
  }
}
