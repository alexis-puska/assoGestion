import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FamilleAccueilComponent } from './list/famille-accueil.component';
import { FamilleAccueilDetailComponent } from './detail/famille-accueil-detail.component';
import { FamilleAccueilUpdateComponent } from './update/famille-accueil-update.component';
import { FamilleAccueilDeleteDialogComponent } from './delete/famille-accueil-delete-dialog.component';
import { FamilleAccueilRoutingModule } from './route/famille-accueil-routing.module';
import { CommonsModule } from 'app/module/commons/commons.module';
import { AdresseModule } from 'app/module/adresse/adresse.module';

@NgModule({
  imports: [SharedModule, FamilleAccueilRoutingModule, CommonsModule, AdresseModule],
  declarations: [
    FamilleAccueilComponent,
    FamilleAccueilDetailComponent,
    FamilleAccueilUpdateComponent,
    FamilleAccueilDeleteDialogComponent,
  ],
  entryComponents: [FamilleAccueilDeleteDialogComponent],
})
export class FamilleAccueilModule {}
