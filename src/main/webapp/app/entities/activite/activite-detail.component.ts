import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IActivite } from 'app/shared/model/activite.model';

@Component({
  selector: 'jhi-activite-detail',
  templateUrl: './activite-detail.component.html',
})
export class ActiviteDetailComponent implements OnInit {
  activite: IActivite | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ activite }) => (this.activite = activite));
  }

  previousState(): void {
    window.history.back();
  }
}
