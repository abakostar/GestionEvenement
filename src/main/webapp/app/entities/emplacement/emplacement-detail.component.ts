import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEmplacement } from 'app/shared/model/emplacement.model';

@Component({
  selector: 'jhi-emplacement-detail',
  templateUrl: './emplacement-detail.component.html',
})
export class EmplacementDetailComponent implements OnInit {
  emplacement: IEmplacement | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ emplacement }) => (this.emplacement = emplacement));
  }

  previousState(): void {
    window.history.back();
  }
}
