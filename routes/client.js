Router.map(function() {
    this.route('play', {
        path: '/play/:_id',
        template: 'board',
        action: function() {
            GAnalytics.pageview('/play');
            this.render('loading');
            var router = this;
            var tableId = this.params._id;
            Meteor.subscribe('tables', tableId, {
                onReady: function() {
                    var table = Tables.findOne(tableId, {reactive: false});
                    if (table) {
                        $.get('/games/' + table.game + '/game.json' + requestSuffix(), function(data) {
                            Session.set('tableId', table._id);
                            Session.set('gameInfo', data);
                            router.render();
                        });
                    } else {
                        window.alert('Invalid table id: ' + tableId);
                        this.redirect('welcome');
                    }
                }
            });
        },
        onAfterAction: function() {
            Session.set('tableId', this.params._id);
        }
    });
    this.route('welcome', {
        path: '/',
        template: 'welcome',
        onBeforeAction: function() {
            GAnalytics.pageview('/welcome');
            Session.set('tableId', null);
        }
    });
});