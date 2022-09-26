import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PointNourrissageComponent } from './list/point-nourrissage.component';
import { PointNourrissageDetailComponent } from './detail/point-nourrissage-detail.component';
import { PointNourrissageUpdateComponent } from './update/point-nourrissage-update.component';
import { PointNourrissageDeleteDialogComponent } from './delete/point-nourrissage-delete-dialog.component';
import { PointNourrissageRoutingModule } from './route/point-nourrissage-routing.module';
import { CommonsModule } from 'app/module/commons/commons.module';
import { ContactModule } from 'app/module/contact/contact.module';
import { AdresseModule } from 'app/module/adresse/adresse.module';

@NgModule({
  imports: [SharedModule, PointNourrissageRoutingModule, CommonsModule, ContactModule, AdresseModule],
  declarations: [
    PointNourrissageComponent,
    PointNourrissageDetailComponent,
    PointNourrissageUpdateComponent,
    PointNourrissageDeleteDialogComponent,
  ],
  entryComponents: [PointNourrissageDeleteDialogComponent],
})
export class PointNourrissageModule {}
