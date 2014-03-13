var gameModuleUnload = function() {
};

unloadModule = function() {
    $('script.hooks').remove();
    gameModuleUnload();
    gameModule = null;
    gameModuleUnload = function() {
    };
};

Deps.autorun(function() {
    if (!is('board.ready', 'sprites.ready'))
        return;

    unloadModule();
    // http://www.nczonline.net/blog/2009/07/28/the-best-way-to-load-external-javascript/
    var script = document.createElement('script');
    script.class = 'hooks';
    script.type = 'text/javascript';
    script.src = '/games/' + getGame() + '/hooks.js' + requestSuffix();
    script.onload = function() {
        if (typeof(gameModule) === 'function') {
            gameModuleUnload = gameModule();
            Session.set('module.ready', true);
        }
    };
    document.getElementsByTagName("head")[0].appendChild(script);
});