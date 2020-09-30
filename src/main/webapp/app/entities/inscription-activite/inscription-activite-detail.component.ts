import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInscriptionActivite } from 'app/shared/model/inscription-activite.model';

@Component({
  selector: 'jhi-inscription-activite-detail',
  templateUrl: './inscription-activite-detail.component.html',
})
export class InscriptionActiviteDetailComponent implements OnInit {
  inscriptionActivite: IInscriptionActivite | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inscriptionActivite }) => (this.inscriptionActivite = inscriptionActivite));
  }

  previousState(): void {
    window.history.back();
  }
}
