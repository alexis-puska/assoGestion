import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IConfigurationContrat } from '../configuration-contrat.model';

@Component({
  selector: 'jhi-configuration-contrat-detail',
  templateUrl: './configuration-contrat-detail.component.html',
})
export class ConfigurationContratDetailComponent implements OnInit {
  configurationContrat: IConfigurationContrat | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ configurationContrat }) => {
      this.configurationContrat = configurationContrat;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
