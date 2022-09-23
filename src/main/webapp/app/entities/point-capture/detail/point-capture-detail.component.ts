import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPointCapture } from '../point-capture.model';

@Component({
  selector: 'jhi-point-capture-detail',
  templateUrl: './point-capture-detail.component.html',
})
export class PointCaptureDetailComponent implements OnInit {
  pointCapture: IPointCapture | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pointCapture }) => {
      this.pointCapture = pointCapture;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
