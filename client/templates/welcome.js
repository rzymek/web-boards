var welcome = Template['welcome'];

welcome['games'] = function() {
    return Session.get('games');
};
welcome.events({
    'click button': function(e) {
        var t = e.currentTarget;
        Session.set('selectedGame', t.value);
    }
});
