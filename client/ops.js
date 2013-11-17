
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

function sleep( sleepDuration ){
    var now = new Date().getTime();
    while(new Date().getTime() < now + sleepDuration){ /* do nothing */ } 
}

NoOp = {
    run: function () {
    },
    undo: function () {
    }
};
/* Op.run results associated with Op data (from Mongo)
 * Op._id are the keys. */
var opsResults = {};
runOp = function(data) {
    (function() {
        var traces = document.getElementById('traces');
        while (traces.firstChild) {
            traces.removeChild(traces.firstChild);
        }
    })();
//    Session.set('selectedPiece',null);
//    console.log('run',data);
    var result = this[data.op].run(data);
    if (result !== undefined) {
        opsResults[data._id] = result;
    }
};
undoOp = function(data) {
    Session.set('selectedPiece',null);
//    console.log('undo',data);
    this[data.op].undo(data, opsResults[data._id]);
    delete opsResults[data._id];
};