
Router.map(function() {
    this.route('export', {
        where: 'server',
        path: '/export/:_id',
        action: function() {
            var tableId = this.params._id;
            var ops = Operations.find({tableId: tableId}).fetch();
            this.response.writeHead(200, {'Content-Type': 'application/json'});
            this.response.end(JSON.stringify(ops));
        }
    });
    this.route('dice', {
        where: 'server',
        path: '/img/dice.svg',
        action: function() {
            this.response.writeHead(200, {'Content-Type': 'image/svg+xml'});
            var svg = Assets.getText('dice.svg');
            svg = svg.replace('{{x}}', this.params.x || '');
            svg = svg.replace('{{y}}', this.params.y || '');
            this.response.end(svg);
        }
    });
    this.route('hooks', {
        where: 'server',
        path: '/games/:game/hooks.js',
        action: function() {
            var fs = Npm.require('fs');
            var response = this.response;
            var filename = '../client/app/games/' + this.params.game + '/hooks.js';
            console.log(filename);
            response.writeHead(200, {'Content-Type': 'text/javascript'});
            if (fs.existsSync(filename)) {
                response.end(fs.readFileSync(filename));
            } else {
                response.end(Assets.getText('defaultHooks.js'));
            }
        }
    });
    this.route('scenarios', {
        where: 'server',
        path: '/games/:game/scenarios.json',
        action: function() {
            var fs = Npm.require('fs');
            var response = this.response;
            var filename = '../client/app/games/' + this.params.game + '/scenarios.json';
            console.log(filename);
            response.writeHead(200, {'Content-Type': 'text/javascript'});
            if (fs.existsSync(filename)) {
                response.end(fs.readFileSync(filename));
            } else {
                response.end("{}");
            }
        }
    });
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