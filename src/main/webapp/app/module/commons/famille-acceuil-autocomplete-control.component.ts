import { Component, forwardRef, OnInit } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { FamilleAccueil } from 'app/entities/famille-accueil/famille-accueil.model';
import { FamilleAccueilService } from 'app/entities/famille-accueil/service/famille-accueil.service';
import { StringUtilsService } from 'app/shared/util/string-utils.service';
import { Observable } from 'rxjs';
import { AbstractAutocompleteControlComponent } from './abstract-autocomplete-control.component';

@Component({
  selector: 'jhi-famille-accueil-autocomplete-control',
  templateUrl: './famille-acceuil-autocomplete-control.component.html',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => FamilleAccueilAutocompleteControlComponent),
      multi: true,
    },
  ],
})
export class FamilleAccueilAutocompleteControlComponent
  extends AbstractAutocompleteControlComponent<FamilleAccueil>
  implements OnInit, ControlValueAccessor
{
  constructor(private api: FamilleAccueilService, private strUtils: StringUtilsService) {
    super();
  }

  ngOnInit(): void {
    super.ngOnInit();
  }

  queryWith(query: string): Observable<FamilleAccueil[]> {
    return this.api.findFamilleAccueilAutocomplete(query);
  }

  getHighlightedQueryInResult(item: FamilleAccueil, searchTerm: string): string {
    if (item.nom) {
      return this.strUtils.highlightTermInString(item.nom, searchTerm);
    } else {
      return '';
    }
  }
}
