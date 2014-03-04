Router.map(function() {
    this.route('play', {
        path: '/play/:_id',
        template: 'board',
        action: function() {
            this.render('loading-board');
            var router = this;
            var tableId = this.params._id;
            Meteor.subscribe('operations', tableId);
            Meteor.call('getTableInfo', tableId, function(error, table) {
                if (table) {
                    $.get('/games/' + table.game + '/game.json', function(data) {
                        data.table = table;
                        Session.set('gameInfo', data);
                        console.log('render play ');
                        router.render();
                    });
                } else {
                    window.alert('Invalid table id: ' + tableId);
                    this.redirect('welcome');
                }
            });
        },
        after: function() {
            console.log('after');
            Session.set('tableId', this.params._id);
        }
    });
    this.route('welcome', {
        path: '/',
        template: 'welcome',
        before: function() {
            Session.set('tableId', null);
        },
        data: function() {
            return {
                msg: this.params.msg
            };
        }
    });
});