import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ChatComponent } from './list/chat.component';
import { ChatDetailComponent } from './detail/chat-detail.component';
import { ChatUpdateComponent } from './update/chat-update.component';
import { ChatDeleteDialogComponent } from './delete/chat-delete-dialog.component';
import { ChatRoutingModule } from './route/chat-routing.module';
import { CommonsModule } from 'app/module/commons/commons.module';
import { AdresseModule } from 'app/module/adresse/adresse.module';
import { VisiteVeterinaireModule } from 'app/module/visite-veterinaire/visite-veterinaire.module';
import { FileSaverModule } from 'ngx-filesaver';

@NgModule({
  imports: [SharedModule, ChatRoutingModule, CommonsModule, AdresseModule, VisiteVeterinaireModule, FileSaverModule],
  declarations: [ChatComponent, ChatDetailComponent, ChatUpdateComponent, ChatDeleteDialogComponent],
  entryComponents: [ChatDeleteDialogComponent],
})
export class ChatModule {}
