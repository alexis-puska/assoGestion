import { IAdresse } from 'app/entities/adresse/adresse.model';

export interface IPointCapture {
  id?: number;
  nom?: string | null;
  adresseCapture?: IAdresse | null;
}

export class PointCapture implements IPointCapture {
  constructor(public id?: number, public nom?: string | null, public adresseCapture?: IAdresse | null) {}
}

export function getPointCaptureIdentifier(pointCapture: IPointCapture): number | undefined {
  return pointCapture.id;
}
