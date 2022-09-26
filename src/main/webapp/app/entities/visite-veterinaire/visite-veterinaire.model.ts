import { IActeVeterinaire } from 'app/entities/acte-veterinaire/acte-veterinaire.model';
import dayjs from 'dayjs/esm';
import { CliniqueVeterinaire } from '../clinique-veterinaire/clinique-veterinaire.model';

export interface IVisiteVeterinaire {
  id?: number;
  dateVisite?: dayjs.Dayjs | null;
  actes?: IActeVeterinaire[] | null;
  cliniqueVeterinaire?: CliniqueVeterinaire | null;
  chatId?: number | null;
}

export class VisiteVeterinaire implements IVisiteVeterinaire {
  constructor(
    public id?: number,
    public dateVisite?: dayjs.Dayjs | null,
    public actes?: IActeVeterinaire[] | null,
    public cliniqueVeterinaireId?: CliniqueVeterinaire | null,
    public chatId?: number | null
  ) {}
}

export function getVisiteVeterinaireIdentifier(visiteVeterinaire: IVisiteVeterinaire): number | undefined {
  return visiteVeterinaire.id;
}
