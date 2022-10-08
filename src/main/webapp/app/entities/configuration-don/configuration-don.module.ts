import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AdresseModule } from 'app/module/adresse/adresse.module';
import { ConfigurationDonRoutingModule } from './route/configuration-don-routing.module';
import { ConfigurationDonUpdateComponent } from './update/configuration-don-update.component';

@NgModule({
  imports: [SharedModule, ConfigurationDonRoutingModule, AdresseModule],
  declarations: [ConfigurationDonUpdateComponent],
  entryComponents: [],
})
export class ConfigurationDonModule {}
