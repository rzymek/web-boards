/// <reference path="../packages/typescript-libs/meteor.d.ts" />
/// <reference path="../common/vassal.d.ts"/>
class TypedSession {
   selectedGame():string { return Session.get('selectedGame');}
   setSelectedGame(v:string) { Session.set('selectedGame',v);}
   gameInfo():Module { return Session.get('gameInfo');}
   setGameInfo(v:Module) { Session.set('gameInfo',v);}
   games():string[] { return Session.get('games');}
   setGames(v:string[]) { Session.set('games',v);}
   piecesCategory():string { return Session.get('piecesCategory');}
   setPiecesCategory(v:string) { Session.set('piecesCategory',v);}
   menuItems():string[] { return Session.get('menuItems');}
   setMenuItems(v:string[]) { Session.set('menuItems',v);}
}
declare var S:TypedSession;
S = new TypedSession();
