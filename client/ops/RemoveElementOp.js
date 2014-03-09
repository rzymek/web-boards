RemoveElementOp = function(data) {
    var element = byId(data.element);
    var parent = element.parentElement;
    element.remove();
    return function() {
        parent.appendChild(element);
    };
};