import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { FullCalendarModule } from '@fullcalendar/angular';
import { CommonsModule } from 'app/module/commons/commons.module';
import { LoginComponent } from './login.component';

@NgModule({
  imports: [SharedModule, CommonsModule, FullCalendarModule, RouterModule.forChild([HOME_ROUTE])],
  declarations: [HomeComponent, LoginComponent],
})
export class HomeModule {}
