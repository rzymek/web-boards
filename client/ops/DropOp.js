DropOp = function(data) {
    var svg = document.getElementById('svg');
    var counter = svg.getElementById(data.counterId);
    counter.parentElement.removeChild(counter);
    return function() {
        counters.appendChild(counter);
    };
};