import { Component, forwardRef, Input, OnInit } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { UserLight } from 'app/entities/user/user-light.model';
import { UserService } from 'app/entities/user/user.service';
import { StringUtilsService } from 'app/shared/util/string-utils.service';
import { Observable } from 'rxjs';
import { AbstractAutocompleteControlComponent } from './abstract-autocomplete-control.component';

@Component({
  selector: 'jhi-user-autocomplete-control',
  templateUrl: './user-autocomplete-control.component.html',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => UserAutocompleteControlComponent),
      multi: true,
    },
  ],
})
export class UserAutocompleteControlComponent
  extends AbstractAutocompleteControlComponent<UserLight>
  implements OnInit, ControlValueAccessor
{
  @Input()
  authorities: string[] = [];

  constructor(private api: UserService, private strUtils: StringUtilsService) {
    super();
  }

  ngOnInit(): void {
    super.ngOnInit();
  }

  queryWith(query: string): Observable<UserLight[]> {
    return this.api.findUserAutocomplete(query, this.authorities);
  }

  getHighlightedQueryInResult(item: UserLight, searchTerm: string): string {
    if (item.firstName) {
      return this.strUtils.highlightTermInString(`${item.firstName} ${item.lastName} `, searchTerm);
    } else {
      return '';
    }
  }
}
