startTour = function() {
    var steps = [{
            // this is a step object
            content: '<p>Click here to start a new "Battle for Moscow" game</p>',
            highlightTarget: true,
            setup: function(tour) {
                var target = $($('#startgame button').filter(function() {
                    return $(this).text() === 'battle-for-moscow';
                })[0]);
                Template.loading.rendered = function() {
                    tour.next();
                };
                return {target: target};
            },
            my: 'bottom center',
            at: 'top center'
        }, {
            content: "<p>Wait till the game fully loads...</p>",
            setup: function(tour) {
                Template.scenario.rendered = function() {
                    Session.set('sceno.ready', Template.scenario.visible())
                }
                Deps.autorun(function(c) {
                    if (is('sceno.ready')) {
                        c.stop();
                        tour.next();
                    }
                });
                return {target: $('#loading')};
            },
            my: 'left center',
            at: 'right center'
        }, {
            content: "<p>This game comes with a predefined setup. Pick it from the list here.</p>",
            setup: function(tour) {
                var t = $($('#scenarioNavBar select')[0]);
                t.bind('change.tour', function() {
                    tour.next();
                });
                return {target: t};
            },
            my: 'top left',
            at: 'bottom center'
        }, {
            content: "<p>Now you can see the scenario setup. To confirm your selection click here. </p>",
            setup: function(tour) {
                var t = $($('#scenarioNavBar button')[0]);
                t.bind('click.tour', function() {
                    tour.next();
                });
                return {target: t};
            },
            my: 'top left',
            at: 'bottom center'
        }, {
            content: "<p>Click on any hex to open the game menu</p>",
            setup: function(tour) {
                var target = $('#h4_4');
                target.bind('click.tour', function() {
                    tour.next();
                });
                return {target: target};
            },
            my: 'bottom center',
            at: 'top center'
        }, {
            content: "<p>Open the pieces panel</p>",
            setup: function(tour) {
                var target = $($('#pieceMenuLayer g').filter(function() {
                    return this.name === 'Pieces';
                })[0]);
                target.bind('click.tour', function() {
                    tour.next();
                });
                return {target: target};
            },
            my: 'left center',
            at: 'right center'
        }];
    var tour = new Tourist.Tour({
        steps: steps,
        tipOptions: {showEffect: 'slidein'}
    });
    tour.start();
};