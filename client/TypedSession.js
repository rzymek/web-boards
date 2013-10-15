/// <reference path="../packages/typescript-libs/meteor.d.ts" />
/// <reference path="../common/vassal.d.ts"/>
var TypedSession = (function () {
    function TypedSession() {
    }
    TypedSession.prototype.selectedGame = function () {
        return Session.get('selectedGame');
    };
    TypedSession.prototype.setSelectedGame = function (v) {
        Session.set('selectedGame', v);
    };
    TypedSession.prototype.gameInfo = function () {
        return Session.get('gameInfo');
    };
    TypedSession.prototype.setGameInfo = function (v) {
        Session.set('gameInfo', v);
    };
    TypedSession.prototype.games = function () {
        return Session.get('games');
    };
    TypedSession.prototype.setGames = function (v) {
        Session.set('games', v);
    };
    TypedSession.prototype.piecesCategory = function () {
        return Session.get('piecesCategory');
    };
    TypedSession.prototype.setPiecesCategory = function (v) {
        Session.set('piecesCategory', v);
    };
    return TypedSession;
})();

S = new TypedSession();
