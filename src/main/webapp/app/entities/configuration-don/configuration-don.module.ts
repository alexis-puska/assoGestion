import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ConfigurationDonComponent } from './list/configuration-don.component';
import { ConfigurationDonDetailComponent } from './detail/configuration-don-detail.component';
import { ConfigurationDonUpdateComponent } from './update/configuration-don-update.component';
import { ConfigurationDonDeleteDialogComponent } from './delete/configuration-don-delete-dialog.component';
import { ConfigurationDonRoutingModule } from './route/configuration-don-routing.module';

@NgModule({
  imports: [SharedModule, ConfigurationDonRoutingModule],
  declarations: [
    ConfigurationDonComponent,
    ConfigurationDonDetailComponent,
    ConfigurationDonUpdateComponent,
    ConfigurationDonDeleteDialogComponent,
  ],
  entryComponents: [ConfigurationDonDeleteDialogComponent],
})
export class ConfigurationDonModule {}
