
Template.controls.menu = function () {
    return S.menuItems();
};

Template.controls.events({
    'click button': function (event) {
        var button = event.currentTarget;
        var txt = button.textContent;
        if (txt === 'Menu') {
            $('div.menu-folded').hide();
            $('div.menu-unfolded').show();
        } else if (txt === 'Hide') {
            $('div.menu-folded').show();
            $('div.menu-unfolded').hide();
        } else {
            menu[txt]();
        }
    }
});
