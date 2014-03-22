Deps.autorun(function() {
    var rt = Router.current();
    if (!rt)
        return;
    var r = rt.route.name;
    console.warn('route: ', r, byId('startgame'), byId('counters'));
});

tour = function() {
    var steps = [{
            // this is a step object
            content: '<p>Click here to start a new "Battle for Moscow" game</p>',
            highlightTarget: true,
            setup: function(t) {
                var target = $($('#startgame button').filter(function() {
                    return $(this).text() === 'battle-for-moscow'
                })[0]);
                Template.loading.rendered = function() {
                    t.next();
                };
                return {target: target};
            },
            my: 'bottom center',
            at: 'top center'
        }, {
            content: "<p>Wait till the game fully loads...</p>",
            target: [100, 100],
            setup: function(tt) {
                Deps.autorun(function(c) {
                    if (is('board.ready')) {
                        c.stop();
                        tt.next();
                    }
                });
            },
            my: 'left center',
            at: 'right center'
        }, {
            content: "<p>Click on any hex to open the game menu</p>",
            setup: function(tt) {
                var t = $('#h4_4');
                t.bind('click.tour', function() {
                    tt.next();
                });
                return {target: t};
            },
            my: 'bottom center',
            at: 'top center'
        }, {
            content: "<p>Open the pieces panel</p>",
            setup: function(tt) {
                var t = $($('#pieceMenuLayer g').filter(function() {
                    return this.name === 'Pieces';
                })[0]);
                t.bind('click.tour', function() {
                    tt.next();
                });
                return {target: t};
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