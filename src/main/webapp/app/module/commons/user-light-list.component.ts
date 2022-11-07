import { Component, Input } from '@angular/core';
import { IUserLight } from './../../entities/user/user-light.model';

@Component({
  selector: 'jhi-user-light-list',
  templateUrl: './user-light-list.component.html',
})
export class UserLightListComponent {
  @Input()
  users: IUserLight[] | null = [];

  constructor() {}
}
