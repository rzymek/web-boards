Router.map(function() {
   this.route('play',{
       path:'/play/:_id',
       action: function () {
           Session.set('tableId', this.params._id);
       }
   });
   this.route('welcome',{
       path: '/',
       action: function() {
           Session.set('tableId', null);
       }
   })
});