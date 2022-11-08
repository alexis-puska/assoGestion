import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'race-chat',
        data: { pageTitle: 'assoGestionApp.raceChat.home.title' },
        loadChildren: () => import('./race-chat/race-chat.module').then(m => m.RaceChatModule),
      },
      {
        path: 'chat',
        data: { pageTitle: 'assoGestionApp.chat.home.title' },
        loadChildren: () => import('./chat/chat.module').then(m => m.ChatModule),
      },
      {
        path: 'famille-accueil',
        data: { pageTitle: 'assoGestionApp.familleAccueil.home.title' },
        loadChildren: () => import('./famille-accueil/famille-accueil.module').then(m => m.FamilleAccueilModule),
      },
      {
        path: 'point-capture',
        data: { pageTitle: 'assoGestionApp.pointCapture.home.title' },
        loadChildren: () => import('./point-capture/point-capture.module').then(m => m.PointCaptureModule),
      },
      {
        path: 'point-nourrissage',
        data: { pageTitle: 'assoGestionApp.pointNourrissage.home.title' },
        loadChildren: () => import('./point-nourrissage/point-nourrissage.module').then(m => m.PointNourrissageModule),
      },
      {
        path: 'clinique-veterinaire',
        data: { pageTitle: 'assoGestionApp.cliniqueVeterinaire.home.title' },
        loadChildren: () => import('./clinique-veterinaire/clinique-veterinaire.module').then(m => m.CliniqueVeterinaireModule),
      },
      {
        path: 'donateur',
        data: { pageTitle: 'assoGestionApp.donateur.home.title' },
        loadChildren: () => import('./donateur/donateur.module').then(m => m.DonateurModule),
      },
      {
        path: 'configuration-asso',
        data: { pageTitle: 'assoGestionApp.configurationAsso.home.title' },
        loadChildren: () => import('./configuration-asso/configuration-asso.module').then(m => m.ConfigurationAssoModule),
      },
      {
        path: 'absence',
        data: { pageTitle: 'assoGestionApp.absence.home.title' },
        loadChildren: () => import('./absence/absence.module').then(m => m.AbsenceModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
