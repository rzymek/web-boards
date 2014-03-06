Router.map(function() {
    this.route('serverRoute', {
        where: 'server',
        path: '/export/:_id',
        action: function() {
            var tableId = this.params._id;
            var ops = Operations.find({tableId: tableId}).fetch();
            this.response.writeHead(200, {'Content-Type': 'application/json'});
            this.response.end(JSON.stringify(ops));
        }
    });
});