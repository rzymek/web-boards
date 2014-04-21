UI.registerHelper('session', function(key){
    return Session.get(key);
});

UI.registerHelper('equals', function(key,value){
    return Session.equals(key, value) ? true : undefined;
});