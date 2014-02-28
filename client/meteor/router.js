Router.map(function() {
    this.route('play', {
        path: '/play/:_id',
        template: 'board',
        before: function() {
            if (Meteor.userId() === null) {
                this.redirect('/?msg=Login to join game '+this.params._id);
            }
        },
        after: function() {
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
    })
});