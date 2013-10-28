/// <reference path="../TypedSession.api.d.ts"/>

var welcome = Template['welcome'];

welcome['games'] = () => S.games();
welcome.events({
    'click button': (e:MouseEvent) => {
        var t = <HTMLButtonElement>e.currentTarget;
        S.setSelectedGame(t.value);
    }
})