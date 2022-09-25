import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { Home } from './home.model';
import { HomeService } from './home.service';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  home: Home | null = null;

  private readonly destroy$ = new Subject<void>();

  constructor(private accountService: AccountService, private router: Router, private homeService: HomeService) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));
    this.homeService.getCounter().subscribe(home => {
      this.home = home.body;
    });
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
