import { IAdresse } from 'app/entities/adresse/adresse.model';
import { TypeLogementEnum } from 'app/entities/enumerations/type-logement-enum.model';
import { IUserLight } from './../user/user-light.model';

export interface IFamilleAccueil {
  id?: number;
  nom?: string | null;
  typeLogement?: TypeLogementEnum | null;
  nombrePiece?: number | null;
  nombreChat?: number | null;
  nombreChien?: number | null;
  adresse?: IAdresse | null;
  referent?: IUserLight | null;
  membres?: IUserLight[] | null;
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
    public referent?: IUserLight | null,
    public membres?: IUserLight[] | null
  ) {}
}

export function getFamilleAccueilIdentifier(familleAccueil: IFamilleAccueil): number | undefined {
  return familleAccueil.id;
}
