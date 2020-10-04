import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEvenement } from 'app/shared/model/evenement.model';

@Component({
  selector: 'jhi-evenement-detail',
  templateUrl: './evenement-detail.component.html',
})
export class EvenementDetailComponent implements OnInit {
  evenement: IEvenement | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ evenement }) => (this.evenement = evenement));
  }

  previousState(): void {
    window.history.back();
  }
}
