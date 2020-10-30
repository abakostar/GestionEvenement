import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IParticipant } from 'app/shared/model/participant.model';

@Component({
  selector: 'jhi-participant-detail',
  templateUrl: './participant-detail.component.html',
})
export class ParticipantDetailComponent implements OnInit {
  participant: IParticipant | null = null;
  // participant:IParticipant;
  predicate: string;
  ascending: boolean;

  constructor(protected activatedRoute: ActivatedRoute) {
    // this.participant = {};
    this.predicate = 'id';
    this.ascending = true;
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ participant }) => (this.participant = participant));
  }

  previousState(): void {
    window.history.back();
  }
}
