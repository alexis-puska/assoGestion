import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DonateurComponent } from './list/donateur.component';
import { DonateurDetailComponent } from './detail/donateur-detail.component';
import { DonateurUpdateComponent } from './update/donateur-update.component';
import { DonateurDeleteDialogComponent } from './delete/donateur-delete-dialog.component';
import { DonateurRoutingModule } from './route/donateur-routing.module';

@NgModule({
  imports: [SharedModule, DonateurRoutingModule],
  declarations: [DonateurComponent, DonateurDetailComponent, DonateurUpdateComponent, DonateurDeleteDialogComponent],
  entryComponents: [DonateurDeleteDialogComponent],
})
export class DonateurModule {}
