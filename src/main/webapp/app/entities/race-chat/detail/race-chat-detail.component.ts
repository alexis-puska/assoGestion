import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRaceChat } from '../race-chat.model';

@Component({
  selector: 'jhi-race-chat-detail',
  templateUrl: './race-chat-detail.component.html',
})
export class RaceChatDetailComponent implements OnInit {
  raceChat: IRaceChat | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ raceChat }) => {
      this.raceChat = raceChat;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
