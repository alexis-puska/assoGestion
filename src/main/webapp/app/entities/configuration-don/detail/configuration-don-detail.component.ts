import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IConfigurationDon } from '../configuration-don.model';

@Component({
  selector: 'jhi-configuration-don-detail',
  templateUrl: './configuration-don-detail.component.html',
})
export class ConfigurationDonDetailComponent implements OnInit {
  configurationDon: IConfigurationDon | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ configurationDon }) => {
      this.configurationDon = configurationDon;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
