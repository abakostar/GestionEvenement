import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInscriptionEvenement } from 'app/shared/model/inscription-evenement.model';

@Component({
  selector: 'jhi-inscription-evenement-detail',
  templateUrl: './inscription-evenement-detail.component.html',
})
export class InscriptionEvenementDetailComponent implements OnInit {
  inscriptionEvenement: IInscriptionEvenement | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inscriptionEvenement }) => (this.inscriptionEvenement = inscriptionEvenement));
  }

  previousState(): void {
    window.history.back();
  }
}
