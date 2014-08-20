var fs = require('fs');
var file = __dirname + '/../stream-info.json';

fs.readFile(file, 'utf8', function(err, data) {
    if (err) {
        console.log('Error: ' + err);
        return;
    }

    var pairs = [];
    JSON.parse(data).map(function(it) {
        return it.nodes;
    }).forEach(function(it) {
        for (var i = 0; i < it.length-1; i++) {
            var v=[it[i],it[i+1]];
            pairs.push(v);
//            pairs.push(v.slice(0).reverse());
        }
    });
    console.log(JSON.stringify(pairs));
});