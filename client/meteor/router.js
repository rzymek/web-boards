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