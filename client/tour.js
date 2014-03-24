startTour = function() {
    var steps = [{
            content: '<p>First thing you need to do is sign in. \n\
                If you don\'t have an account already, you can create one here as well.</p>',
            highlightTarget: true,
            target: $('#signinup'),
            setup: function(tour) {
                //Calling tour.next() from `setup` causes an error in tourist.js
                //Defered call is ok.
                Meteor.defer(function() {
                    Deps.autorun(function(c) {
                        if (Meteor.userId()) {
                            tour.next();
                            c.stop();
                        }
                    });
                });
            },
            my: 'top right',
            at: 'bottom center'
        }, {
            // this is a step object
            content: '<p>Click here to start a new "Battle for Moscow" game</p>',
            highlightTarget: true,
            setup: function(tour) {

                var buttons = $('#startgame button');
                buttons.map(function(idx, btn) {
                    btn.disabled = ($(btn).text() !== 'battle-for-moscow');
                    console.log(btn, btn.disabled);
                });
                var target = $(buttons.filter(function() {
                    return $(this).text() === 'battle-for-moscow';
                })[0]);
                Template.loading.rendered = function() {
                    Deps.autorun(function() {
                        if (getGame() === 'battle-for-moscow') {
                            tour.next();
                        }
                    })
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
            content: "<p>Now you can see the scenario setup. To confirm your selection click here. \n\
            Making any move on the board will also confirm scenario selection.</p>",
            setup: function(tour) {
                var c = Operations.find({op: 'ScenarioOp'}).observe({
                    added: function() {
                        tour.next();
                        c.stop();
                    }
                });
                return {target: $($('#scenarioNavBar button')[0])};
            },
            my: 'top left',
            at: 'bottom center'
        }, {
            content: "<p>Click on a counter to select it.</p>",
            setup: function(tour) {
                var orig = selectById;
                selectById = function(id) {
                    selectById = orig;
                    orig(id);
                    tour.next();
                };
                return {target: $('#_11')}
            },
            my: 'top left',
            at: 'bottom center'

        }, {
            content: "<p>Click on a target hex to move the selected counter.</p>",
            setup: function(tour) {
                var c = Operations.find({op: 'MoveOp'}).observe({
                    added: function() {
                        tour.next();
                        c.stop();
                    }
                });
                return {target: $('#h7_3')}
            },
            my: 'top left',
            at: 'bottom center'
        }, {
            content: "<p>Click on any empty hex to open the game menu</p>",
            setup: function(tour) {
                var orig = showMenu;
                showMenu = function(hex) {
                    orig(hex);
                    tour.next();
                    showMenu = orig;
                }
                return {target: $('#h10_6')};
            },
            my: 'bottom center',
            at: 'top center'
        }, {
            content: "<p>Here you can undo any action you've done. Try it. (Ctrl+Z also works) </p>",
            setup: function(tour) {
                var target = $($('#pieceMenuLayer g').filter(function() {
                    return this.name === 'Undo';
                })[0]);
                var c = Operations.find().observe({
                    removed: function() {
                        tour.next();
                        c.stop();
                    }
                });
                return {target: target};
            },
            my: 'left center',
            at: 'right center'
        }, {
            content: "<p>Now, open the pieces panel</p>",
            setup: function(tour) {
                var target = $($('#pieceMenuLayer g').filter(function() {
                    return this.name === 'Pieces';
                })[0]);
                target.bind('click.tour', function() {
                    Session.set('piecesCategory', 'Special');
                    tour.next();
                });
                return {target: target};
            },
            my: 'left center',
            at: 'right center'
        }, {
            content: "<p>The first category holds some 'special' counters. Press next to see their descriptions.</p>",
            setup: function(tour) {
                return {target: $('#piecesPanel')};
            },
            nextButton: true,
            my: 'right center',
            at: 'left bottom'
        }, {
            content: "<p>This action does a roll of two 6-sided dice (2d6). Click on it select it. Then click on any hex on the board to execute it.</p>",
            setup: function(tour, options) {
                var c = Operations.find({op: 'RollOp'}).observe({
                    added: function(doc) {
                        options.opId = doc._id;
                        tour.next();
                        c.stop();
                    }
                });
                return {target: $('#roll_2d6')};
            },
            my: 'bottom right',
            at: 'top center'
        }, {
            content: "<p>The result of the dice roll is shown on the 'counter'. Click on it to acknowledge and hide it.</p>",
            setup: function(tour, options) {
                var t = $(byId(options.opId));
                var c = Operations.find({op: 'RemoveElementOp', element: options.opId}).observe({
                    added: function() {
                        tour.next();
                        c.stop();
                    }
                });
                return {target: t};
            },
            my: 'bottom center',
            at: 'top center'
        }, {
            content: "<p>The other usefull special counter allows you to place a note on the board. \n\
                Select it and click on the board to enter your comment.</p>",
            setup: function(tour, options) {
                var c = Operations.find({op: 'NoteOp'}).observe({
                    added: function(doc) {
                        options.opId = doc._id;
                        tour.next();
                        c.stop();
                    }
                });
                return {target: $('#note')};
            },
            my: 'bottom right',
            at: 'top center'
        }, {
            content: "<p>Click on it to acknowledge and hide it.</p>",
            setup: function(tour, options) {
                var t = $(byId(options.opId));
                var c = Operations.find({op: 'RemoveElementOp', element: options.opId}).observe({
                    added: function() {
                        tour.next();
                        c.stop();
                    }
                });
                return {target: t};
            },
            my: 'bottom center',
            at: 'top center'
        }, {
            content: "<p>The other categories hold standard game counters. </p>",
            setup: function(tour) {
                var t = $('#piecesPanel');
                t.click();
                return {target: t};
            },
            nextButton: true,
            my: 'right bottom',
            at: 'left center'
        }, {
            content: "<p>Now, we'll open the counter menu. Select a counter. </p>",
            setup: function(tour, options) {
                var orig = selectById;
                selectById = function(id) {
                    selectById = orig;
                    options.selectedCounter = byId(id);
                    orig(id);
                    tour.next();
                };
                return {target: $('#_11')}
            },
            my: 'right center',
            at: 'left center'
        }, {
            content: "<p>Click again on an already selected counter to show the it's menu. </p>",
            setup: function(tour) {
                var orig = showPieceMenu;
                showPieceMenu = function(arg) {
                    showPieceMenu = orig;
                    orig(arg);
                    tour.next();
                };
                return {target: $(byId(getSelectedId()))}
            },
            my: 'left center',
            at: 'right center'
        }, {
            content: "<p>Here you can flip it ... </p>",
            setup: function(tour, options) {
                if (!showingPieceMenu())
                    showPieceMenu(options.selectedCounter);
                var target = $($('#pieceMenuLayer g').filter(function() {
                    return this.name === 'Flip';
                })[0]);
                var c = Operations.find({op: 'FlipOp', counterId: options.selectedCounter.id}).observe({
                    added: function() {
                        tour.next();
                        c.stop();
                    }
                });
                return {target: target};
            },
            nextButton: true,
            my: 'left center',
            at: 'right center'
        }, {
            content: "<p>... or remove it from the board.</p>",
            setup: function(tour, options) {
                if (!showingPieceMenu())
                    showPieceMenu(options.selectedCounter);
                var target = $($('#pieceMenuLayer g').filter(function() {
                    return this.name === 'Remove';
                })[0]);
                var c = Operations.find({op: 'RemovePieceOp', counterId: options.selectedCounter.id}).observe({
                    added: function() {
                        tour.next();
                        c.stop();
                    }
                });
                return {target: target};
            },
            nextButton: true,
            my: 'left center',
            at: 'right center'
        }, {
            content: "<p>To leave this game press the Back button in your browser.</p>",
            target: [100, 0],
            setup: function(tour) {
                Deps.autorun(function(c) {
                    if (Router.current().route.name === 'welcome') {
                        tour.stop();
                        c.stop();
                    }
                });
            },
            teardown: function() {
                Router.go('welcome');
            },
            nextButton: true,
            my: 'top left',
            at: 'top center',
        }];
    var tour = new Tourist.Tour({
        steps: steps,
        stepOptions: {},
        tipOptions: {showEffect: 'slidein'}
    });
    tour.start();
};