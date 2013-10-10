jsSetup = function () {
    var panel = $('#panel');
    panel.draggable({
        start:function () {
            panel.height(panel.height());
        }
    });
};

Meteor.call('dirs', function(err, res){
    console.log(res);
});
