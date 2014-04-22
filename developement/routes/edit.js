Router.map(function() {
    this.route('edit', {
        path: '/edit/:game',
        template: 'edit',
        action: function() {
            Session.set('editingGame', this.params.game);
            var router = this;
            $.get('/games/' + this.params.game + '/game.json' + requestSuffix(), function(data) {
                Session.set('gameInfo', data);
                router.render();
            });
        },
        onStop: function() {
            Session.set('edit.ready', false);
            unbindKeys();
        }
    });
    this.route('edit.export', {
        where: 'server',
        path: '/edit/:game/path-info.json',
        action: function() {
            this.response.writeHead(200, {'Content-Type': 'text/javascript'});
            var out = Edit.find({
                game: this.params.game
            }, {
                fields: {_id: 0, timestamp: 0, game: 0}
            }).fetch();
            this.response.end(JSON.stringify(out, null, ' '));
        }
    });
});