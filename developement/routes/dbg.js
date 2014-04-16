Router.map(function() {
    this.route('dbg-users', {
        where: 'server',
        path: '/usr',
        action: function() {
            this.response.writeHead(200, {'Content-Type': 'text/plain'});
            var users = {};
            Meteor.users.find({}, {fields: {emails: 1, createdAt: 1}}).forEach(function(user) {
                users[user.emails[0].address] = user.createdAt;
            });
            this.response.end(JSON.stringify(users, null, ' '));
        }
    });
    this.route('dbg-tables', {
        where: 'server',
        path: '/tables',
        action: function() {
            var response = this.response;
            response.writeHead(200, {'Content-Type': 'text/plain'});
            Tables.find().forEach(function(table) {
                response.write(JSON.stringify(table) + "\n");
            });
            response.end();
        }
    });
    this.route('errorlog', {
        where: 'server',
        path: '/errors',
        action: function() {
            this.response.writeHead(200, {'Content-Type': 'text/javascript'});
            var errors = ErrorLog.find({}, {
                sort: [['time', 'desc']]
            }).fetch();
            this.response.end(JSON.stringify(errors, null, ' '));
        }
    });
});