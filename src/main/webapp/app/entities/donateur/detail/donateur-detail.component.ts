import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDonateur } from '../donateur.model';

@Component({
  selector: 'jhi-donateur-detail',
  templateUrl: './donateur-detail.component.html',
})
export class DonateurDetailComponent implements OnInit {
  donateur: IDonateur | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ donateur }) => {
      this.donateur = donateur;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
