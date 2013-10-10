jsSetup = function () {
    var panel = $('#panel');
    panel.draggable({
        start:function () {
            panel.height(panel.height());
        }
    });
};