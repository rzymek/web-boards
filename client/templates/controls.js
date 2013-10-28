/// <reference path="../TypedSession.api.d.ts"/>
/// <reference path="../api/core.d.ts"/>
/// <reference path="../../packages/typescript-libs/jquery.d.ts" />
var controls = Template['controls'];

controls['menu'] = function () {
    return S.menuItems();
};

controls.events({
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
            ctx.menu[txt]();
        }
    }
});
