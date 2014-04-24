var fs = require('fs');
var file = __dirname + '/../stream-info.json';

fs.readFile(file, 'utf8', function(err, data) {
    if (err) {
        console.log('Error: ' + err);
        return;
    }

    var pairs = {};
    JSON.parse(data).map(function(it) {
        return it.nodes;
    }).forEach(function(it) {
        for (var i = 0; i < it.length-1; i++) {
            pairs[it[i]] = it[i+1];
        }
    });
    var keys = Object.keys(pairs);
    console.log(JSON.stringify(pairs));
});