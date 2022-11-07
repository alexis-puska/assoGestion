import { IAdresse } from 'app/entities/adresse/adresse.model';
import { IUserLight } from './../user/user-light.model';

export interface IPointNourrissage {
  id?: number;
  nom?: string | null;
  adresse?: IAdresse | null;
  contacts?: IUserLight[] | null;
}

export class PointNourrissage implements IPointNourrissage {
  constructor(public id?: number, public nom?: string | null, public adresse?: IAdresse | null, public contacts?: IUserLight[] | null) {}
}

export function getPointNourrissageIdentifier(pointNourrissage: IPointNourrissage): number | undefined {
  return pointNourrissage.id;
}
