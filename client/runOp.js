/* Op.run results associated with Op data (from Mongo)
 * Op._id are the keys. */
var undoFunctions = {};

runOp = function(data) {
    Session.set('replayIndex', null);
    removeChildren(byId('traces'));
    undoFunctions[data._id] = this[data.op](data);
};

undoOp = function(data) {
    selectById(null);
    var undo = undoFunctions[data._id];
    if(undo !== undefined) 
        undo();
    delete undoFunctions[data._id];
};