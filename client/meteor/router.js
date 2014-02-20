Router.map(function() {
    this.route('play', {
        path: '/play/:_id',
        template: 'board',
        action: function() {
            if(Meteor.userId() === null) {
                this.redirect('welcome');
            }else{
                Session.set('tableId', this.params._id);
                this.render();
            }            
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