import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVisiteVeterinaire } from '../visite-veterinaire.model';

@Component({
  selector: 'jhi-visite-veterinaire-detail',
  templateUrl: './visite-veterinaire-detail.component.html',
})
export class VisiteVeterinaireDetailComponent implements OnInit {
  visiteVeterinaire: IVisiteVeterinaire | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ visiteVeterinaire }) => {
      this.visiteVeterinaire = visiteVeterinaire;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
