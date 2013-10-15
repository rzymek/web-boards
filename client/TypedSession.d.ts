/// <reference path="../packages/typescript-libs/meteor.d.ts" />
/// <reference path="../common/vassal.d.ts"/>
declare class TypedSession {
   public selectedGame():string;
   public setSelectedGame(v:string):void;
   public gameInfo():Module;
   public setGameInfo(v:Module):void;
   public games():string[];
   public setGames(v:string[]):void;
}
declare var S:TypedSession;