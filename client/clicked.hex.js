hexClicked = function(e) {
    if (byId('svg').panning)
        return;
    hideStackSelector();

    var hex = e.currentTarget;
    console.log(hex.id);

    var selectedId = getSelectedId();
    if (selectedId === null) {
        //nothing is currently selected
        if (showingPieceMenu()) {
            hidePieceMenu();
            return;
        }
        var stack = hex.stack;
        if (stack === undefined || stack === null || stack.length === 0) {
            //empty hex -> show global menu
            showMenu(hex);
            return;
        }
        if (stack.length > 1) {
            //there's a stack, show stack selector
            showStackSelector(hex, stack);
        } else if (stack.length === 1) {
            //single counter in hex -> select it
            selectById(stack[0].id);
        }
        return;
    } else {
        var selectedCounter = byId(selectedId);
        if (selectedCounter.position && selectedCounter.position.id === hex.id) {
            togglePieceMenu(selectedCounter);
        } else if (selectedCounter.getAttribute('category') === 'Special') {
            var target = null;
            if (hex.stack && hex.stack.length > 0)
                target = hex.stack[0];
            Special[selectedCounter.id].action(hex, target);
        } else if (isOnBoard(selectedCounter)) {
            Operations.insert({
                op: 'MoveOp',
                counter: selectedCounter.id,
                to: hex.id
            });
        } else {
            function scale(dim, scale) {
                return (scale === undefined) ? dim : {
                    width: dim.width * scale,
                    height: dim.height * scale
                };
            }
            Operations.insert({
                op: 'PlaceOp',
                category: selectedCounter.getAttribute('category'),
                name: selectedCounter.getAttribute('name'),
                hexid: hex.id
            });
        }
    }
};
