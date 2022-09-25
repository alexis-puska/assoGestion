export interface IHome {
  nbCliniqueVeterinaire: number;
  nbChat: number;
  nbPointNourrissage: number;
  nbPointCapture: number;
  nbFamilleAccueil: number;
  nbDonateur: number;
}

export class Home implements IHome {
  constructor(
    public nbCliniqueVeterinaire: number,
    public nbChat: number,
    public nbPointNourrissage: number,
    public nbPointCapture: number,
    public nbFamilleAccueil: number,
    public nbDonateur: number
  ) {}
}
