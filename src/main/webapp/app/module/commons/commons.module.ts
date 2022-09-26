import { NgModule } from '@angular/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { SharedModule } from 'app/shared/shared.module';
import { FamilleAccueilAutocompleteControlComponent } from './famille-acceuil-autocomplete-control.component';
import { PointCaptureAutocompleteControlComponent } from './point-capture-autocomplete-control.component';
import { RaceChatAutocompleteControlComponent } from './race-chat-autocomplete-control.component';

@NgModule({
  imports: [SharedModule, NgSelectModule],
  declarations: [
    FamilleAccueilAutocompleteControlComponent,
    RaceChatAutocompleteControlComponent,
    PointCaptureAutocompleteControlComponent,
  ],
  exports: [FamilleAccueilAutocompleteControlComponent, RaceChatAutocompleteControlComponent, PointCaptureAutocompleteControlComponent],
  entryComponents: [],
})
export class CommonsModule {}
