import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IChat } from '../chat.model';
import { ChatService } from '../service/chat.service';

@Component({
  templateUrl: './chat-delete-dialog.component.html',
})
export class ChatDeleteDialogComponent {
  chat?: IChat;

  constructor(protected chatService: ChatService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.chatService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
