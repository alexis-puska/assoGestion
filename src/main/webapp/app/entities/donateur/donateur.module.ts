import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DonateurComponent } from './list/donateur.component';
import { DonateurDetailComponent } from './detail/donateur-detail.component';
import { DonateurUpdateComponent } from './update/donateur-update.component';
import { DonateurDeleteDialogComponent } from './delete/donateur-delete-dialog.component';
import { DonateurRoutingModule } from './route/donateur-routing.module';
import { CommonsModule } from 'app/module/commons/commons.module';
import { AdresseModule } from 'app/module/adresse/adresse.module';
import { FileSaverModule } from 'ngx-filesaver';

@NgModule({
  imports: [SharedModule, DonateurRoutingModule, CommonsModule, AdresseModule, FileSaverModule],
  declarations: [DonateurComponent, DonateurDetailComponent, DonateurUpdateComponent, DonateurDeleteDialogComponent],
  entryComponents: [DonateurDeleteDialogComponent],
})
export class DonateurModule {}
