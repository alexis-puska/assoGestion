import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPointNourrissage } from '../point-nourrissage.model';

@Component({
  selector: 'jhi-point-nourrissage-detail',
  templateUrl: './point-nourrissage-detail.component.html',
})
export class PointNourrissageDetailComponent implements OnInit {
  pointNourrissage: IPointNourrissage | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pointNourrissage }) => {
      this.pointNourrissage = pointNourrissage;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
