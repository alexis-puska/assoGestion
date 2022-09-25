import { Component, forwardRef, OnInit } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { PointCapture } from 'app/entities/point-capture/point-capture.model';
import { PointCaptureService } from 'app/entities/point-capture/service/point-capture.service';
import { StringUtilsService } from 'app/shared/util/string-utils.service';
import { Observable } from 'rxjs';
import { AbstractAutocompleteControlComponent } from './abstract-autocomplete-control.component';

@Component({
  selector: 'jhi-point-capture-autocomplete-control',
  templateUrl: './point-capture-autocomplete-control.component.html',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => PointCaptureAutocompleteControlComponent),
      multi: true,
    },
  ],
})
export class PointCaptureAutocompleteControlComponent
  extends AbstractAutocompleteControlComponent<PointCapture>
  implements OnInit, ControlValueAccessor
{
  constructor(private api: PointCaptureService, private strUtils: StringUtilsService) {
    super();
  }

  ngOnInit(): void {
    super.ngOnInit();
  }

  queryWith(query: string): Observable<PointCapture[]> {
    return this.api.findPointCaptureAutocomplete(query);
  }

  getHighlightedQueryInResult(item: PointCapture, searchTerm: string): string {
    if (item.nom) {
      return this.strUtils.highlightTermInString(
        `${item.nom} - ${item?.adresseCapture?.numero} ${item?.adresseCapture?.rue} ${item?.adresseCapture?.codePostale} ${item?.adresseCapture?.ville} `,
        searchTerm
      );
    } else {
      return '';
    }
  }
}
