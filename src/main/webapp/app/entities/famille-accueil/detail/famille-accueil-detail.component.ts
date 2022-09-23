import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFamilleAccueil } from '../famille-accueil.model';

@Component({
  selector: 'jhi-famille-accueil-detail',
  templateUrl: './famille-accueil-detail.component.html',
})
export class FamilleAccueilDetailComponent implements OnInit {
  familleAccueil: IFamilleAccueil | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ familleAccueil }) => {
      this.familleAccueil = familleAccueil;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
