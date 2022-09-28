import { Component, forwardRef, OnInit } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { CliniqueVeterinaire } from 'app/entities/clinique-veterinaire/clinique-veterinaire.model';
import { CliniqueVeterinaireService } from 'app/entities/clinique-veterinaire/service/clinique-veterinaire.service';
import { StringUtilsService } from 'app/shared/util/string-utils.service';
import { Observable } from 'rxjs';
import { AbstractAutocompleteControlComponent } from './abstract-autocomplete-control.component';

@Component({
  selector: 'jhi-clinique-veterinaire-autocomplete-control',
  templateUrl: './clinique-veterinaire-autocomplete-control.component.html',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => CliniqueVeterinaireAutocompleteControlComponent),
      multi: true,
    },
  ],
})
export class CliniqueVeterinaireAutocompleteControlComponent
  extends AbstractAutocompleteControlComponent<CliniqueVeterinaire>
  implements OnInit, ControlValueAccessor
{
  constructor(private api: CliniqueVeterinaireService, private strUtils: StringUtilsService) {
    super();
  }

  ngOnInit(): void {
    super.ngOnInit();
  }

  queryWith(query: string): Observable<CliniqueVeterinaire[]> {
    return this.api.findCliniqueVeterinaireAutocomplete(query);
  }

  getHighlightedQueryInResult(item: CliniqueVeterinaire, searchTerm: string): string {
    if (item.nom) {
      return this.strUtils.highlightTermInString(item.nom, searchTerm);
    } else {
      return '';
    }
  }
}
