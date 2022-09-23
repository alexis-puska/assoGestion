import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RaceChatComponent } from './list/race-chat.component';
import { RaceChatDetailComponent } from './detail/race-chat-detail.component';
import { RaceChatUpdateComponent } from './update/race-chat-update.component';
import { RaceChatDeleteDialogComponent } from './delete/race-chat-delete-dialog.component';
import { RaceChatRoutingModule } from './route/race-chat-routing.module';

@NgModule({
  imports: [SharedModule, RaceChatRoutingModule],
  declarations: [RaceChatComponent, RaceChatDetailComponent, RaceChatUpdateComponent, RaceChatDeleteDialogComponent],
  entryComponents: [RaceChatDeleteDialogComponent],
})
export class RaceChatModule {}
