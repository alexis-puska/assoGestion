import { ExtendedProps } from './extended-props.model';

export interface IEvent {
  start: Date;
  end: Date;
  title: string;
  backgroundColor: string;
  extendedProps: ExtendedProps;
}

export class Event implements IEvent {
  constructor(
    public start: Date,
    public end: Date,
    public title: string,
    public backgroundColor: string,
    public extendedProps: ExtendedProps
  ) {}
}
