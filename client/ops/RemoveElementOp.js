RemoveElementOp = function(data) {
    var elements = data.elements || [];
    if (data.element) {
        elements.push(data.element);
    }
    var backup=[];
    elements.forEach(function(elementId) {        
        var element = byId(elementId);
        backup.push({
            element: element,
            parent: element.parentElement
        });
        element.remove();
    });
    return function() {
        backup.forEach(function(it){
            it.parent.appendChild(it.element);
        });
    };
};