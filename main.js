Operations = new Meteor.Collection('operations');

if (Meteor.isClient) {
    Ops = {ops:[]};
    Ops.register = function(name, func) {
        this.ops[name] = func;
    }
    
    Ops.register('move', function(args) {
        console.log("run: move: ",args);
    });
    
    Template.main.game = 'bastogne';
    Template.main.board = {w: 6800, h: 4400};
    Operations.find().observeChanges({
       added: function(id,fields) {           
           var op =  Ops.ops[fields.name];
           op(fields.args);
       }
    });
}

if (Meteor.isServer) {
    Meteor.startup(function() {
        Operations.remove({});
        Operations.insert({name:'move', args:{what:'cnt', to:'5.35'}});
    });
}

