tour2 = function() {
    var calloutMgr = hopscotch.getCalloutManager();
    calloutMgr.createCallout({
        id: 's1',
        title: "Start a new game",
        content: "Click on 'battle-for-moscow' button to start a new game",
        target: $('#startgame button').filter(function() {
            return $(this).text() === 'battle-for-moscow'
        })[0],
        placement: "left",
    });
    $('#startgame button').filter(function() {
        return $(this).text() === 'battle-for-moscow'
    }).click(function() {
        console.log('got to s2');
        calloutMgr.createCallout({
            id: 's2',
            title: "Open the main menu",
            content: "Click on any hex to open the game menu",
            target: 'h4_4',
            placement: "top",
        });
    });
//    var currentTour = {
//        id: "start-game",
//        steps: [
//            {
//            },
//            {
//                title: "Open the main menu",
//                content: "Click on any hex to open the game menu",
//                target: 'h4_4',
//                placement: "top",
//                onShow: function() {
//                    $('#h4_4').click(function() {
//                        currentTour.steps[2].target = $('#pieceMenuLayer g').filter(function() {
//                            return this.name === 'Pieces';
//                        })[0]
//                    })
//                }
//            }
//        ]
//    };
//    hopscotch.startTour(currentTour);
};