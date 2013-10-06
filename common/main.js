
if (Meteor.isClient) {
    Ops = {ops:[]};
    Ops.register = function(name, func) {
        this.ops[name] = func;
    }
    
    Ops.register('move', function(args) {
        console.log("run: move: ",args);
    });

    Operations.find().observeChanges({
       added: function(id,fields) {           
           var op =  Ops.ops[fields.name];
           op(fields.args);
       }
    });
}

