import { IAdresse } from 'app/entities/adresse/adresse.model';
import { IContact } from 'app/entities/contact/contact.model';
import { TypeLogementEnum } from 'app/entities/enumerations/type-logement-enum.model';

export interface IFamilleAccueil {
  id?: number;
  nom?: string | null;
  typeLogement?: TypeLogementEnum | null;
  nombrePiece?: number | null;
  nombreChat?: number | null;
  nombreChien?: number | null;
  adresse?: IAdresse | null;
  contacts?: IContact[] | null;
}

export class FamilleAccueil implements IFamilleAccueil {
  constructor(
    public id?: number,
    public nom?: string | null,
    public typeLogement?: TypeLogementEnum | null,
    public nombrePiece?: number | null,
    public nombreChat?: number | null,
    public nombreChien?: number | null,
    public adresse?: IAdresse | null,
    public contacts?: IContact[] | null
  ) {}
}

export function getFamilleAccueilIdentifier(familleAccueil: IFamilleAccueil): number | undefined {
  return familleAccueil.id;
}
