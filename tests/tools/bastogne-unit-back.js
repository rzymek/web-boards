var fs = require('fs');
var glob = require('glob');
var base = __dirname + '/../../public/games/bastogne/';
var file = base + 'game.json';

fs.readFile(file, 'utf8', function(err, data) {
    if (err) {
        console.log('Error: ' + err);
        return;
    }

    data = JSON.parse(data).pieces.map(function(it) {
        return it.list.filter(function(c) {
            return c.images.length === 2;
        }).map(function(c) {
            return {
                name: it.category + '\x1e' + c.name,
                front: c.images[0],
                back: c.images[1]
            };
        });
    }).reduce(function(a, b) {
        return a.concat(b);
    }).map(function(it) {
        var pattern = base + 'images/b/*,*,*,' + it.back;
        it.pattern = pattern;
        it.back_info_files = glob.sync(pattern);
        if (it.back_info_files.length > 1) {
            throw it.back_info_files;
        }
        return it;
    }).filter(function(it) {
        return it.back_info_files.length !== 0;
    }).map(function(it) {
        return {
            name: it.name,
            back_info_file: it.back_info_files[0]
        };
    }).map(function(it) {
        return {
            name: it.name,
            info: it.back_info_file.replace(/([0-9]*,[0-9]*,[0-9]*).*/, '$1')
        };
    }).map(function(it) {
        return {
            name: it.name,
            info: it.info.substr(it.info.lastIndexOf('/') + 1).split(/,/)
        };
    }).map(function(it) {
        return {
            name: it.name,
            back: {
                attack: it.info[0],
                defence: it.info[1],
                movement: it.info[2]
            }
        };
    });
    console.log(JSON.stringify(data, null, 2));
});
