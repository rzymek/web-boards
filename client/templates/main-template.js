/// <reference path="../TypedSession.api.d.ts"/>
Template.main.gameSelected = function() {
    return !Session.equals('selectedGame', null);
};
