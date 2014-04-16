UI.registerHelper('session', function(key){
    return Session.get(key);
});

UI.registerHelper('selected', function(key){
    return Session.equals(key, this._id) ? true : undefined; 
});