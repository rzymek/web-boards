
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
            var dirname = '../client/app/games/' + this.params.game + '/src';
            console.log(filename);
            response.writeHead(200, {'Content-Type': 'text/javascript'});
            if (fs.statSync(dirname).isDirectory()) {
                var walk = function(dir) {
                    var list = fs.readdirSync(dir)
                    list.forEach(function(file) {
                        file = dir + '/' + file;
                        var stat = fs.statSync(file)
                        if (stat && stat.isDirectory())
                            walk(file);
                        else if(file.endsWith('.js')) {
                            response.write(fs.readFileSync(file), 'utf-8');
                            response.write('\n', 'utf-8');
                        }
                    })
                };
                response.write('(function(){\n');
                walk(dirname);
                response.write('\n})();');
            } else if (fs.existsSync(filename)) {
                response.end(fs.readFileSync(filename), 'utf-8');
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
    this.route('edit.export', {
        where: 'server',
        path: '/edit/:game/export',
        action: function() {
            this.response.writeHead(200, {'Content-Type': 'text/javascript'});
            var out = Edit.find({
                game: this.params.game
            }, {
                fields: {from: 1, to: 1, type: 1, _id: 0}
            }).fetch();
            this.response.end(JSON.stringify(out, null, ' '));
        }
    });
});