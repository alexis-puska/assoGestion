import { NgModule } from '@angular/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { SharedModule } from 'app/shared/shared.module';
import { CliniqueVeterinaireAutocompleteControlComponent } from './clinique-veterinaire-autocomplete-control.component';
import { FamilleAccueilAutocompleteControlComponent } from './famille-acceuil-autocomplete-control.component';
import { PointCaptureAutocompleteControlComponent } from './point-capture-autocomplete-control.component';
import { RaceChatAutocompleteControlComponent } from './race-chat-autocomplete-control.component';

@NgModule({
  imports: [SharedModule, NgSelectModule],
  declarations: [
    FamilleAccueilAutocompleteControlComponent,
    RaceChatAutocompleteControlComponent,
    PointCaptureAutocompleteControlComponent,
    CliniqueVeterinaireAutocompleteControlComponent,
  ],
  exports: [
    FamilleAccueilAutocompleteControlComponent,
    RaceChatAutocompleteControlComponent,
    PointCaptureAutocompleteControlComponent,
    CliniqueVeterinaireAutocompleteControlComponent,
  ],
  entryComponents: [],
})
export class CommonsModule {}
