import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import {IParticipantEvenement, ParticipantEvenement} from 'app/shared/model/participant-evenement.model';
import { IParticipant } from 'app/shared/model/participant.model';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import {ParticipantService} from "../participant.service";
import {IEvenement} from "../../../shared/model/evenement.model";
import {InscriptionActiviteDialogComponent} from "../inscription-activite/inscription-activite-dialog.component";

@Component({
  selector: 'jhi-inscription-evenement',
  templateUrl: './inscription-evenement.component.html',
  styleUrls: ['./inscription-evenement.component.scss'],
})
export class InscriptionEvenementComponent implements OnInit {
  participant: IParticipant | null = null;
  evenements: IEvenement [] | null = null;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected participantService: ParticipantService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
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
    this.participantService
        .findByCurrentUser()
        .subscribe((res: HttpResponse<IParticipant>) => {
          this.participant = res.body;
          this.evenements = this.participant && this.participant.evenements ? this.participant.evenements : [];
        });
  }

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

  saveInscriptionEvent(evenement: IEvenement, index: number, participantId: number): void {
    const participantEvenement =  new ParticipantEvenement(participantId, evenement.id, !evenement.registered);
    this.participantService.addParticipant(participantEvenement).subscribe((res: HttpResponse<IParticipantEvenement>) => {
      if (res && res.body && this.participant && this.participant.evenements && this.participant.evenements.length > index) {
        this.participant.evenements[index].registered = res.body.registered;
      }
    });
  }

  detailActivities(participant: IParticipant, evenement: IEvenement): void {
    const modalRef = this.modalService.open(InscriptionActiviteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.participant = participant;
    modalRef.componentInstance.evenement = evenement;
  }
}
