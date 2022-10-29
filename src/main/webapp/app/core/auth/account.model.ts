export class Account {
  constructor(
    public activated: boolean,
    public authorities: string[],
    public email: string,
    public telephone: string,
    public firstName: string | null,
    public langKey: string,
    public lastName: string | null,
    public login: string,
    public imageUrl: string | null
  ) {}
}
