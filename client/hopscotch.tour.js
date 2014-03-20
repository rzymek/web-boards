TOUR1 = {
    id: "signin",
    steps: [
        {
            title: "Welcome",
            content: "You neet to sign in first. If you don't have an account you can also create one here.",
            target: "signinup",
            placement: "left"
        },
    ]
};

tour2 = function() {
    var currentTour = {
        id: "start-game",
        steps: [
            {
                title: "Start a new game",
                content: "Click on 'battle-for-moscow' button to start a new game",
                target: $('#startgame button').filter(function() {
                    return $(this).text() === 'battle-for-moscow'
                })[0],
                placement: "left",
            },
            {
                title: "Open the main menu",
                content: "Click on any hex to open the game menu",
                target: 'h4_4',
                placement: "top",
                onShow: function() {
                    $('#h4_4').click(function() {
                        currentTour.steps[2].target = $('#pieceMenuLayer g').filter(function() {
                            return this.name === 'Pieces';
                        })[0]
                    })
                }
            }
        ]
    };
    hopscotch.startTour(currentTour);
};