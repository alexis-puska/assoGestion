import dayjs from 'dayjs/esm';
import { IAdresse } from 'app/entities/adresse/adresse.model';
import { PaiementEnum } from 'app/entities/enumerations/paiement-enum.model';

export interface IContrat {
  id?: number;
  nom?: string | null;
  prenom?: string | null;
  email?: string | null;
  telephone?: string | null;
  cout?: number | null;
  paiement?: PaiementEnum | null;
  dateContrat?: dayjs.Dayjs | null;
  adresseAdoptant?: IAdresse | null;
}

export class Contrat implements IContrat {
  constructor(
    public id?: number,
    public nom?: string | null,
    public prenom?: string | null,
    public email?: string | null,
    public telephone?: string | null,
    public cout?: number | null,
    public paiement?: PaiementEnum | null,
    public dateContrat?: dayjs.Dayjs | null,
    public adresseAdoptant?: IAdresse | null
  ) {}
}

export function getContratIdentifier(contrat: IContrat): number | undefined {
  return contrat.id;
}
