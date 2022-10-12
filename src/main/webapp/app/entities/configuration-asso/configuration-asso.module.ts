import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AdresseModule } from 'app/module/adresse/adresse.module';
import { ConfigurationAssoRoutingModule } from './route/configuration-asso-routing.module';
import { ConfigurationAssoUpdateComponent } from './update/configuration-asso-update.component';

@NgModule({
  imports: [SharedModule, ConfigurationAssoRoutingModule, AdresseModule],
  declarations: [ConfigurationAssoUpdateComponent],
  entryComponents: [],
})
export class ConfigurationAssoModule {}
