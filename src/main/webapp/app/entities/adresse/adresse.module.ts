import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AdresseDeleteDialogComponent } from './delete/adresse-delete-dialog.component';
import { AdresseDetailComponent } from './detail/adresse-detail.component';
import { AdresseComponent } from './list/adresse.component';
import { AdresseRoutingModule } from './route/adresse-routing.module';
import { AdresseUpdateComponent } from './update/adresse-update.component';

@NgModule({
  imports: [SharedModule, AdresseRoutingModule],
  declarations: [AdresseComponent, AdresseDetailComponent, AdresseUpdateComponent, AdresseDeleteDialogComponent],
  entryComponents: [AdresseDeleteDialogComponent],
})
export class AdresseModule {}
