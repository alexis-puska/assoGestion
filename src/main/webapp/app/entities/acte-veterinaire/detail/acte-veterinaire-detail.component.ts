import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IActeVeterinaire } from '../acte-veterinaire.model';

@Component({
  selector: 'jhi-acte-veterinaire-detail',
  templateUrl: './acte-veterinaire-detail.component.html',
})
export class ActeVeterinaireDetailComponent implements OnInit {
  acteVeterinaire: IActeVeterinaire | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ acteVeterinaire }) => {
      this.acteVeterinaire = acteVeterinaire;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
