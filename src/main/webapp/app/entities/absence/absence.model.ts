import dayjs from 'dayjs/esm';
import { IUserLight } from './../user/user-light.model';

export interface IAbsence {
  id?: number;
  motif?: string | null;
  start?: dayjs.Dayjs;
  end?: dayjs.Dayjs;
  user?: IUserLight;
}

export class Absence implements IAbsence {
  constructor(
    public id?: number,
    public motif?: string | null,
    public start?: dayjs.Dayjs,
    public end?: dayjs.Dayjs,
    public user?: IUserLight
  ) {}
}

export function getAbsenceIdentifier(absence: IAbsence): number | undefined {
  return absence.id;
}
