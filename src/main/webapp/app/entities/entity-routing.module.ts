import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'chat',
        data: { pageTitle: 'assoGestionApp.chat.home.title' },
        loadChildren: () => import('./chat/chat.module').then(m => m.ChatModule),
      },
      {
        path: 'visite-veterinaire',
        data: { pageTitle: 'assoGestionApp.visiteVeterinaire.home.title' },
        loadChildren: () => import('./visite-veterinaire/visite-veterinaire.module').then(m => m.VisiteVeterinaireModule),
      },
      {
        path: 'famille-accueil',
        data: { pageTitle: 'assoGestionApp.familleAccueil.home.title' },
        loadChildren: () => import('./famille-accueil/famille-accueil.module').then(m => m.FamilleAccueilModule),
      },
      {
        path: 'adresse',
        data: { pageTitle: 'assoGestionApp.adresse.home.title' },
        loadChildren: () => import('./adresse/adresse.module').then(m => m.AdresseModule),
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
        path: 'race-chat',
        data: { pageTitle: 'assoGestionApp.raceChat.home.title' },
        loadChildren: () => import('./race-chat/race-chat.module').then(m => m.RaceChatModule),
      },
      {
        path: 'acte-veterinaire',
        data: { pageTitle: 'assoGestionApp.acteVeterinaire.home.title' },
        loadChildren: () => import('./acte-veterinaire/acte-veterinaire.module').then(m => m.ActeVeterinaireModule),
      },
      {
        path: 'contrat',
        data: { pageTitle: 'assoGestionApp.contrat.home.title' },
        loadChildren: () => import('./contrat/contrat.module').then(m => m.ContratModule),
      },
      {
        path: 'configuration-asso',
        data: { pageTitle: 'assoGestionApp.configurationAsso.home.title' },
        loadChildren: () => import('./configuration-asso/configuration-asso.module').then(m => m.ConfigurationAssoModule),
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
        path: 'contact',
        data: { pageTitle: 'assoGestionApp.contact.home.title' },
        loadChildren: () => import('./contact/contact.module').then(m => m.ContactModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
