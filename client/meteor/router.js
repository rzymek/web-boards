Router.map(function() {
    this.route('play', {
        path: '/play/:_id',
        template: 'board',
        before: function() {
            Session.set('tableId', this.params._id);
        }
    });
    this.route('welcome', {
        path: '/',
        template: 'welcome',
        before: function() {
            Session.set('tableId', null);
        }
    })
});


Deps.autorun(function() {
    var tableId = Session.get('tableId');
    if (tableId !== null) {
        var table = Tables.findOne(tableId);
        if (!table) {
            Session.set('selectedGame', null);
            if (window.confirm('Do you want to join this game?')) {
                Meteor.call('join', tableId, function(error, result) {
                    if(!result) {
                        window.alert('No such game');
                        Router.go('welcome');
                    }
                });
            } else {
                Router.go('welcome');
            }
        } else {
            Session.set('selectedGame', table.game);
        }
    } else {
        Session.set('selectedGame', null);
    }
});