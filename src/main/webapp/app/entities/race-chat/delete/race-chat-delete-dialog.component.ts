import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRaceChat } from '../race-chat.model';
import { RaceChatService } from '../service/race-chat.service';

@Component({
  templateUrl: './race-chat-delete-dialog.component.html',
})
export class RaceChatDeleteDialogComponent {
  raceChat?: IRaceChat;

  constructor(protected raceChatService: RaceChatService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.raceChatService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
