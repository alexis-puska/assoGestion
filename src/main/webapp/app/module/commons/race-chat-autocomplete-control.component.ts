import { Component, forwardRef, OnInit } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { RaceChat } from 'app/entities/race-chat/race-chat.model';
import { RaceChatService } from 'app/entities/race-chat/service/race-chat.service';
import { StringUtilsService } from 'app/shared/util/string-utils.service';
import { Observable } from 'rxjs';
import { AbstractAutocompleteControlComponent } from './abstract-autocomplete-control.component';

@Component({
  selector: 'jhi-race-chat-autocomplete-control',
  templateUrl: './race-chat-autocomplete-control.component.html',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => RaceChatAutocompleteControlComponent),
      multi: true,
    },
  ],
})
export class RaceChatAutocompleteControlComponent
  extends AbstractAutocompleteControlComponent<RaceChat>
  implements OnInit, ControlValueAccessor
{
  constructor(private api: RaceChatService, private strUtils: StringUtilsService) {
    super();
  }

  ngOnInit(): void {
    super.ngOnInit();
  }

  queryWith(query: string): Observable<RaceChat[]> {
    return this.api.findRaceChatAutocomplete(query);
  }

  getHighlightedQueryInResult(item: RaceChat, searchTerm: string): string {
    if (item.libelle) {
      return this.strUtils.highlightTermInString(item.libelle, searchTerm);
    } else {
      return '';
    }
  }
}
