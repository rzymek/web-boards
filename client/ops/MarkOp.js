MarkOp = function(data) {
    var counter = byId(data.counterId);
    var style = counter.style;
    var initial = style.filter;
    style.filter = (initial === '') ? 'url(#mark)' : '';
    return function() {
        style.filter = initial;
    }
};