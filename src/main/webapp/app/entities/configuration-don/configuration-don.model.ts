import { IAdresse } from 'app/entities/adresse/adresse.model';

export interface IConfigurationDon {
  id?: number;
  denomination?: string | null;
  objet?: string | null;
  signataire?: string | null;
  adresse?: IAdresse | null;
}

export class ConfigurationDon implements IConfigurationDon {
  constructor(
    public id?: number,
    public denomination?: string | null,
    public objet?: string | null,
    public signataire?: string | null,
    public adresse?: IAdresse | null
  ) {}
}

export function getConfigurationDonIdentifier(configurationDon: IConfigurationDon): number | undefined {
  return configurationDon.id;
}
