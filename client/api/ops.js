
removeFromStack = function(target, img) {
    target.stack = target.stack.filter(function(it) {
        return it !== img;
    });
};

addToStack = function(hex, counter) {
    counter.position = hex;
    if (hex.stack)
        hex.stack.push(counter);
    else
        hex.stack = [counter];
};

// Op.run results associated with Op data (from Mongo)
// Op._id are the keys.
var opsResults = {};
runOp = function(data) {
    var result = this[data.op].run(data);
    if (result !== undefined) {
        opsResults[data._id] = result;
    }
};
undoOp = function(data) {
    this[data.op].undo(data, opsResults[data._id]);
    //TODO: remove delete opsResults at data._id
};