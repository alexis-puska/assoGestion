import { Event } from './event.model';

export interface ICalendar {
  events: Event;
}

export class Calendar implements ICalendar {
  constructor(public events: Event) {}
}
