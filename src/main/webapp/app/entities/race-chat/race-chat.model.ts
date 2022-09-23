export interface IRaceChat {
  id?: number;
  libelle?: string | null;
}

export class RaceChat implements IRaceChat {
  constructor(public id?: number, public libelle?: string | null) {}
}

export function getRaceChatIdentifier(raceChat: IRaceChat): number | undefined {
  return raceChat.id;
}
