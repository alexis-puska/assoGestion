import { IFamilleAccueil } from 'app/entities/famille-accueil/famille-accueil.model';
import { IPointNourrissage } from 'app/entities/point-nourrissage/point-nourrissage.model';

export interface IContact {
  id?: number;
  nom?: string;
  prenom?: string;
  mail?: string | null;
  telMobile?: string | null;
  telFixe?: string | null;
  familleAccueil?: IFamilleAccueil | null;
  pointNourrissage?: IPointNourrissage | null;
}

export class Contact implements IContact {
  constructor(
    public id?: number,
    public nom?: string,
    public prenom?: string,
    public mail?: string | null,
    public telMobile?: string | null,
    public telFixe?: string | null,
    public familleAccueil?: IFamilleAccueil | null,
    public pointNourrissage?: IPointNourrissage | null
  ) {}
}

export function getContactIdentifier(contact: IContact): number | undefined {
  return contact.id;
}
