import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PointCaptureComponent } from './list/point-capture.component';
import { PointCaptureDetailComponent } from './detail/point-capture-detail.component';
import { PointCaptureUpdateComponent } from './update/point-capture-update.component';
import { PointCaptureDeleteDialogComponent } from './delete/point-capture-delete-dialog.component';
import { PointCaptureRoutingModule } from './route/point-capture-routing.module';
import { CommonsModule } from 'app/module/commons/commons.module';
import { AdresseModule } from 'app/module/adresse/adresse.module';

@NgModule({
  imports: [SharedModule, PointCaptureRoutingModule, CommonsModule, AdresseModule],
  declarations: [PointCaptureComponent, PointCaptureDetailComponent, PointCaptureUpdateComponent, PointCaptureDeleteDialogComponent],
  entryComponents: [PointCaptureDeleteDialogComponent],
})
export class PointCaptureModule {}
