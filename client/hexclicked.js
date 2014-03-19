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
        // there's was a counter selected before this click
        var counter = byId(selectedId);
        if (counter.position && counter.position.id === hex.id) {
            //clicked on a selected counter
            if (!showingPieceMenu()) {
                //first click on a selected counter -> show counter menu here
                hidePieceMenu();
                showPieceMenu(counter);
            } else {
                //second click on a selected counter -> deselect it
                hidePieceMenu();
                selectById(null);
            }
            return;
        }

        hidePieceMenu();

        if (isOnBoard(counter)) {
            Operations.insert({
                op: 'MoveOp',
                counter: counter.id,
                to: hex.id
            });
        } else {
            if (counter.getAttribute('category') === 'Special') {
                var target = null;
                if(hex.stack && hex.stack.length > 0)
                    target = hex.stack[0];
                Special[counter.id].action(hex, target);
            } else {
                function scale(dim, scale) {
                    return (scale === undefined) ? dim : {
                        width: dim.width * scale,
                        height: dim.height * scale
                    };
                }
                Operations.insert({
                    op: 'PlaceOp',
                    category: counter.getAttribute('category'),
                    name: counter.getAttribute('name'),
                    hexid: hex.id
                });
            }
        }
    }
};
