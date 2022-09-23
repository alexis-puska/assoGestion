import { IAdresse } from 'app/entities/adresse/adresse.model';
import { IContact } from 'app/entities/contact/contact.model';

export interface IPointNourrissage {
  id?: number;
  nom?: string | null;
  adresse?: IAdresse | null;
  contacts?: IContact[] | null;
}

export class PointNourrissage implements IPointNourrissage {
  constructor(public id?: number, public nom?: string | null, public adresse?: IAdresse | null, public contacts?: IContact[] | null) {}
}

export function getPointNourrissageIdentifier(pointNourrissage: IPointNourrissage): number | undefined {
  return pointNourrissage.id;
}
