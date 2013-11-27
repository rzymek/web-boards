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