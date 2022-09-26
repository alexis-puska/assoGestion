export interface IActeVeterinaire {
  id?: number;
  libelle?: string | null;
  visiteVeterinaireId?: number | null;
}

export class ActeVeterinaire implements IActeVeterinaire {
  constructor(public id?: number, public libelle?: string | null, public visiteVeterinaire?: number | null) {}
}

export function getActeVeterinaireIdentifier(acteVeterinaire: IActeVeterinaire): number | undefined {
  return acteVeterinaire.id;
}
