import { NgModule } from '@angular/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { SharedModule } from 'app/shared/shared.module';
import { AdresseEdit2Component } from './adresse-edit2.component';
import { AdresseViewComponent } from './adresse-view.component';

@NgModule({
  imports: [SharedModule, NgSelectModule],
  declarations: [AdresseViewComponent, AdresseEdit2Component],
  exports: [AdresseViewComponent, AdresseEdit2Component],
  entryComponents: [],
})
export class AdresseModule {}
