import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ConfigurationContratComponent } from './list/configuration-contrat.component';
import { ConfigurationContratDetailComponent } from './detail/configuration-contrat-detail.component';
import { ConfigurationContratUpdateComponent } from './update/configuration-contrat-update.component';
import { ConfigurationContratDeleteDialogComponent } from './delete/configuration-contrat-delete-dialog.component';
import { ConfigurationContratRoutingModule } from './route/configuration-contrat-routing.module';

@NgModule({
  imports: [SharedModule, ConfigurationContratRoutingModule],
  declarations: [
    ConfigurationContratComponent,
    ConfigurationContratDetailComponent,
    ConfigurationContratUpdateComponent,
    ConfigurationContratDeleteDialogComponent,
  ],
  entryComponents: [ConfigurationContratDeleteDialogComponent],
})
export class ConfigurationContratModule {}
