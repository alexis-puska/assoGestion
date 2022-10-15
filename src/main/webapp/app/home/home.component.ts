import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { Home } from './home.model';
import { HomeService } from './home.service';
import { CalendarOptions, EventApi } from '@fullcalendar/angular';
import { CalendarService } from 'app/entities/calendar/calendar.service';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  home: Home | null = null;

  events: any[] = [];

  calendarOptions: CalendarOptions = {};

  private readonly destroy$ = new Subject<void>();

  constructor(
    private accountService: AccountService,
    private router: Router,
    private homeService: HomeService,
    private calendarService: CalendarService
  ) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => {
        this.account = account;
        this.calendarOptions = {
          initialView: 'dayGridMonth',
          eventClick: this.handleDateClick.bind(this),
          locale: 'fr',
          firstDay: 1,
          headerToolbar: {
            right: 'prev,today,next',
            center: 'title',
            left: '',
          },
          titleFormat: { year: 'numeric', month: 'long', day: 'numeric' },
          buttonText: {
            today: "Aujourd'hui",
          },
          datesSet: this.dateRenderTrigger.bind(this),
        };
      });
    this.homeService.getCounter().subscribe(home => {
      this.home = home.body;
    });
  }

  dateRenderTrigger(info: any): void {
    if (this.account?.login) {
      this.calendarService.getEvent(this.account.login, info.start, info.end).subscribe(res => {
        this.calendarOptions.events = res.body?.events;
      });
    }
  }

  handleDateClick(arg: any): void {
    const event = arg.event as EventApi;
    console.log(event.extendedProps, event.start, event.end);
  }

  handleEventChange(arg: any): void {
    console.log('event change', arg);
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
