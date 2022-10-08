import { IAdresse } from 'app/entities/adresse/adresse.model';

export interface IConfigurationDon {
  id?: number;
  denomination?: string | null;
  objet?: string | null;
  objet1?: string | null;
  objet2?: string | null;
  adresse?: IAdresse | null;
  hasSignature?: boolean;
  deleteSignature?: boolean;
}

export class ConfigurationDon implements IConfigurationDon {
  constructor(
    public id?: number,
    public denomination?: string | null,
    public objet?: string | null,
    public objet1?: string | null,
    public objet2?: string | null,
    public adresse?: IAdresse | null,
    public hasSignature?: boolean,
    public deleteSignature?: boolean
  ) {}
}
