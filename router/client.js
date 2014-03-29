Router.map(function() {
    this.route('play', {
        path: '/play/:_id',
        template: 'board',
        action: function() {
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
            console.log('after');
            Session.set('tableId', this.params._id);
        }
    });
    this.route('edit', {
        path: '/edit/:game',
        template: 'edit',
        action: function() {
            Session.set('editingGame',this.params.game);
            var router = this;
            $.get('/games/' + this.params.game + '/game.json' + requestSuffix(), function(data) {
                Session.set('gameInfo', data);
                router.render();
            });
        }
    });
    this.route('welcome', {
        path: '/',
        template: 'welcome',
        onBeforeAction: function() {
            Session.set('tableId', null);
        }
    });
});