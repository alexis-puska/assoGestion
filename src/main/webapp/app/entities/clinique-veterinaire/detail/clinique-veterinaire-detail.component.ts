import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICliniqueVeterinaire } from '../clinique-veterinaire.model';

@Component({
  selector: 'jhi-clinique-veterinaire-detail',
  templateUrl: './clinique-veterinaire-detail.component.html',
})
export class CliniqueVeterinaireDetailComponent implements OnInit {
  cliniqueVeterinaire: ICliniqueVeterinaire | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cliniqueVeterinaire }) => {
      this.cliniqueVeterinaire = cliniqueVeterinaire;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
