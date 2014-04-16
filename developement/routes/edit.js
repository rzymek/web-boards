Router.map(function() {
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
});