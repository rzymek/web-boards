Deps.autorun(function() {
    var game = getGame();
    if (!game)
        return;
    Session.set('scenarios', null);
    $.get('/games/' + game + '/scenarios.json'+requestSuffix(), function(data) {
        if(typeof(data) === 'object')
            Session.set('scenarios', data);
    });
});