RemovePieceOp = function(data) {
    var counter = byId(data.counterId);
    var hex = counter.position;
    removeFromStack(hex, counter);
    counter.remove();
    alignStack(hex);
    return function() {
        byId('counters').appendChild(counter);
        addToStack(hex, counter);
        alignStack(hex);
    };
};