
saveSceno = function() {
    var counters = byId('counters').children;
    console.log('----------------');
    var setup = [];
    for (var i = 0; i < counters.length; i++) {
        var c = counters[i];
        setup.push([
            c.category,
            c.name,
            c.position.id
        ]);
    }
    return setup;
};

showSceno = function (){    
    var output = saveSceno();
    window.open('data:application/json;' + (window.btoa?'base64,'+btoa(JSON.stringify(output)):JSON.stringify(output)));
};