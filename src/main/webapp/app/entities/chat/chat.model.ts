import dayjs from 'dayjs/esm';
import { IContrat } from 'app/entities/contrat/contrat.model';
import { IVisiteVeterinaire } from 'app/entities/visite-veterinaire/visite-veterinaire.model';
import { IFamilleAccueil } from 'app/entities/famille-accueil/famille-accueil.model';
import { IPointCapture } from 'app/entities/point-capture/point-capture.model';
import { IRaceChat } from 'app/entities/race-chat/race-chat.model';
import { TypeIdentificationEnum } from 'app/entities/enumerations/type-identification-enum.model';
import { PoilEnum } from 'app/entities/enumerations/poil-enum.model';
import { SexeEnum } from 'app/entities/enumerations/sexe-enum.model';

export interface IChat {
  id?: number;
  nom?: string;
  typeIdentification?: TypeIdentificationEnum | null;
  identification?: string | null;
  dateNaissance?: dayjs.Dayjs;
  description?: string | null;
  robe?: string;
  poil?: PoilEnum;
  sexe?: SexeEnum;
  sterilise?: boolean | null;
  contrat?: IContrat | null;
  visites?: IVisiteVeterinaire[] | null;
  famille?: IFamilleAccueil | null;
  adresseCapture?: IPointCapture | null;
  race?: IRaceChat | null;
  hasPhoto?: boolean;
  deletePhoto?: boolean;
}

export class Chat implements IChat {
  constructor(
    public id?: number,
    public nom?: string,
    public typeIdentification?: TypeIdentificationEnum | null,
    public identification?: string | null,
    public dateNaissance?: dayjs.Dayjs,
    public description?: string | null,
    public robe?: string,
    public poil?: PoilEnum,
    public sexe?: SexeEnum,
    public sterilise?: boolean | null,
    public contrat?: IContrat | null,
    public visites?: IVisiteVeterinaire[] | null,
    public famille?: IFamilleAccueil | null,
    public adresseCapture?: IPointCapture | null,
    public race?: IRaceChat | null,
    public hasPhoto?: boolean,
    public deletePhoto?: boolean
  ) {}
}

export function getChatIdentifier(chat: IChat): number | undefined {
  return chat.id;
}
