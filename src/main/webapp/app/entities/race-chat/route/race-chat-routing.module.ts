import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RaceChatComponent } from '../list/race-chat.component';
import { RaceChatDetailComponent } from '../detail/race-chat-detail.component';
import { RaceChatUpdateComponent } from '../update/race-chat-update.component';
import { RaceChatRoutingResolveService } from './race-chat-routing-resolve.service';

const raceChatRoute: Routes = [
  {
    path: '',
    component: RaceChatComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RaceChatDetailComponent,
    resolve: {
      raceChat: RaceChatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RaceChatUpdateComponent,
    resolve: {
      raceChat: RaceChatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RaceChatUpdateComponent,
    resolve: {
      raceChat: RaceChatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(raceChatRoute)],
  exports: [RouterModule],
})
export class RaceChatRoutingModule {}
