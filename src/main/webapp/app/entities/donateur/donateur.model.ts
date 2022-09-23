import { IAdresse } from 'app/entities/adresse/adresse.model';
import { FormeDonEnum } from 'app/entities/enumerations/forme-don-enum.model';
import { NatureDon } from 'app/entities/enumerations/nature-don.model';
import { NumeraireDonEnum } from 'app/entities/enumerations/numeraire-don-enum.model';

export interface IDonateur {
  id?: number;
  nom?: string | null;
  prenom?: string | null;
  montant?: number | null;
  sommeTouteLettre?: string | null;
  formeDon?: FormeDonEnum | null;
  natureDon?: NatureDon | null;
  numeraireDon?: NumeraireDonEnum | null;
  adresse?: IAdresse | null;
}

export class Donateur implements IDonateur {
  constructor(
    public id?: number,
    public nom?: string | null,
    public prenom?: string | null,
    public montant?: number | null,
    public sommeTouteLettre?: string | null,
    public formeDon?: FormeDonEnum | null,
    public natureDon?: NatureDon | null,
    public numeraireDon?: NumeraireDonEnum | null,
    public adresse?: IAdresse | null
  ) {}
}

export function getDonateurIdentifier(donateur: IDonateur): number | undefined {
  return donateur.id;
}
