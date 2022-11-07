export interface IUserLight {
  id?: number;
  firstName?: string;
  lastName?: string;
  email?: string;
  telephone?: string;
}

export class UserLight implements IUserLight {
  constructor(public id: number, public firstName: string, public lastName: string, public email: string, public telephone: string) {}
}

export function getUserIdentifier(user: IUserLight): number | undefined {
  return user.id;
}
