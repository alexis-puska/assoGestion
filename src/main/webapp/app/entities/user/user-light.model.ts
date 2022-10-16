export interface IUserLight {
  id?: number;
  firstName?: string;
  lastName?: string;
}

export class UserLight implements IUserLight {
  constructor(public id: number, public firstName: string, public lastName: string) {}
}

export function getUserIdentifier(user: IUserLight): number | undefined {
  return user.id;
}
