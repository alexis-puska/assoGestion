export interface IConfigurationContrat {
  id?: number;
  content?: string | null;
}

export class ConfigurationContrat implements IConfigurationContrat {
  constructor(public id?: number, public content?: string | null) {}
}

export function getConfigurationContratIdentifier(configurationContrat: IConfigurationContrat): number | undefined {
  return configurationContrat.id;
}
