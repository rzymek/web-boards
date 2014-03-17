
saveSceno = function() {
    var ops = Operations.find().fetch().map(function(it){
        delete it.createdAt;
        delete it.tableId;
        return it;
    });
    return ops;
};

showSceno = function (){    
    var output = saveSceno();
    window.open('data:application/json;' + (window.btoa?'base64,'+btoa(JSON.stringify(output)):JSON.stringify(output)));
};