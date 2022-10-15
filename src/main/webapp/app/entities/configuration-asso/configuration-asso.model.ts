import { IAdresse } from 'app/entities/adresse/adresse.model';

export interface IConfigurationAsso {
  id?: number;
  denomination?: string | null;
  objet?: string | null;
  objet1?: string | null;
  objet2?: string | null;
  adresse?: IAdresse | null;
  siret?: string | null;
  email?: string | null;
  telephone?: string | null;
  hasSignature?: boolean;
  deleteSignature?: boolean;
  hasLogo?: boolean;
  deleteLogo?: boolean;
}

export class ConfigurationAsso implements IConfigurationAsso {
  constructor(
    public id?: number,
    public denomination?: string | null,
    public objet?: string | null,
    public objet1?: string | null,
    public objet2?: string | null,
    public adresse?: IAdresse | null,
    public siret?: string | null,
    public email?: string | null,
    public telephone?: string | null,
    public hasSignature?: boolean,
    public deleteSignature?: boolean,
    public hasLogo?: boolean,
    public deleteLogo?: boolean
  ) {}
}
