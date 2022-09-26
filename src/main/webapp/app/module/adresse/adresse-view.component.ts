import { Component, Input } from '@angular/core';
import { IAdresse } from 'app/entities/adresse/adresse.model';

@Component({
  selector: 'jhi-adresse-view',
  templateUrl: './adresse-view.component.html',
})
export class AdresseViewComponent {
  @Input()
  adresse: IAdresse | null = null;
}
