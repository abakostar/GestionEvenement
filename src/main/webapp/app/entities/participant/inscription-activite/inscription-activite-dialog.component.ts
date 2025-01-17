import {Component, OnInit} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager} from 'ng-jhipster';

import {IParticipant} from 'app/shared/model/participant.model';
import {ParticipantService} from '../participant.service';
import {IEvenement} from "../../../shared/model/evenement.model";
import {HttpResponse} from "@angular/common/http";
import {IParticipantActivite, ParticipantActivite} from "../../../shared/model/participant-activite.model";
import {ITEMS_PER_PAGE} from "../../../shared/constants/pagination.constants";

@Component({
  templateUrl: './inscription-activite-dialog.component.html',
})
export class InscriptionActiviteDialogComponent implements OnInit {
  participant?: IParticipant;
  evenement?: IEvenement;
  participantActivites: IParticipantActivite [] | null = null;
  defaultRoles: string[];
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected participantService: ParticipantService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
    this.defaultRoles = ['Participant','Organisateur','Sponsor']
  }

  ngOnInit(): void {
    if(this.evenement){
      this.participantService
        .findActivitesByEvenementId(this.evenement.id)
        .subscribe((res: HttpResponse<IParticipantActivite[]>) => {
          this.participantActivites = res.body;
        });
    }
  }

  reset(): void {
    this.page = 0;
  }

  loadPage(page: number): void {
    this.page = page;
  }


  cancel(): void {
    this.activeModal.dismiss();
  }

  trackId(index: number, item: IParticipantActivite): number {
    return item.activite.id!;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  setRegistered(participantActive: IParticipantActivite, index: number): void {
    if (this.participantActivites && this.participantActivites.length > index) {
      this.participantActivites[index].registered = !participantActive.registered;
    }
  }

  saveParticipantActivite(participantActive: IParticipantActivite, index: number): void {
    if (this.participantActivites && this.participantActivites.length > index) {
      const participantId = this.participant ? this.participant.id : 0;
      const participantActivite =  new ParticipantActivite({}, participantId, participantActive.activite.id, participantActive.registered, this.participantActivites[index].role);
      this.participantService.addActivite(participantActivite).subscribe((res: HttpResponse<IParticipantActivite>) => {
        if (res && res.body && this.participantActivites && this.participantActivites.length > index) {
          this.participantActivites[index].registered = res.body.registered;
        }
      });
    }
  }

}
