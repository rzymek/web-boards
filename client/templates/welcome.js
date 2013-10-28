/// <reference path="../TypedSession.api.d.ts"/>
var welcome = Template['welcome'];

welcome['games'] = function () {
    return S.games();
};
welcome.events({
    'click button': function (e) {
        var t = e.currentTarget;
        S.setSelectedGame(t.value);
    }
});
