export interface IExtendedProps {
  id: number;
  entity: string;
}

export class ExtendedProps implements IExtendedProps {
  constructor(public id: number, public entity: string) {}
}
