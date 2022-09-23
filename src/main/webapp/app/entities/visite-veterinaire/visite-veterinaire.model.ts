import dayjs from 'dayjs/esm';
import { IActeVeterinaire } from 'app/entities/acte-veterinaire/acte-veterinaire.model';
import { ICliniqueVeterinaire } from 'app/entities/clinique-veterinaire/clinique-veterinaire.model';
import { IChat } from 'app/entities/chat/chat.model';

export interface IVisiteVeterinaire {
  id?: number;
  dateVisite?: dayjs.Dayjs | null;
  actes?: IActeVeterinaire[] | null;
  cliniqueVeterinaire?: ICliniqueVeterinaire | null;
  chat?: IChat | null;
}

export class VisiteVeterinaire implements IVisiteVeterinaire {
  constructor(
    public id?: number,
    public dateVisite?: dayjs.Dayjs | null,
    public actes?: IActeVeterinaire[] | null,
    public cliniqueVeterinaire?: ICliniqueVeterinaire | null,
    public chat?: IChat | null
  ) {}
}

export function getVisiteVeterinaireIdentifier(visiteVeterinaire: IVisiteVeterinaire): number | undefined {
  return visiteVeterinaire.id;
}
