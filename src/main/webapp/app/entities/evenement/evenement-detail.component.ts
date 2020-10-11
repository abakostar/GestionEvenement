import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEvenement } from 'app/shared/model/evenement.model';

@Component({
  selector: 'jhi-evenement-detail',
  templateUrl: './evenement-detail.component.html',
})
export class EvenementDetailComponent implements OnInit {
  evenement: IEvenement | null = null;
  predicate: string;
  ascending: boolean;
  viewpgme: boolean;
  viewparticipants: boolean;
  namebutton: String;

  constructor(protected activatedRoute: ActivatedRoute) {
    this.predicate = 'id';
    this.ascending = true;
    this.viewpgme = false;
    this.viewparticipants = true;
    this.namebutton = 'Voir Programme';
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ evenement }) => (this.evenement = evenement));
  }

  previousState(): void {
    window.history.back();
  }
  programview(): void {
    this.viewpgme = !this.viewpgme;
    this.viewparticipants = !this.viewparticipants;
    this.namebutton = 'Voir Participants';
  }
}
