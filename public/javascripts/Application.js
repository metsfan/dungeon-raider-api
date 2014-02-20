var app = null;

var Application = Backbone.Router.extend({

    routes: {
        "":                    "index",

        // Spells
        "spells" :              SpellController.index,
        "spells/:class" :       SpellController.classIndex,
        "spell/:id" :           SpellController.editSpell,
        "spell" :               SpellController.createSpell,
    },

    index: function() {

    },

    navigate: function(fragment, options) {
        if (options.args) {
            $.cookie("postData", JSON.stringify(options.args));
            $.cookie("postUrl", fragment);
        } else {
            $.removeCookie("postData");
        }

        Backbone.Router.prototype.navigate.apply(this, arguments);
    },

    _extractParameters: function(route, fragment) {
        var args = Backbone.Router.prototype._extractParameters.apply(this, arguments);

        var postUrl = $.cookie("postUrl");
        if (postUrl != fragment) {
            $.removeCookie("postData");
        }

        var params = {}
        var backArg = args.pop();
        if (backArg != null) {
            var qstringParams = Util.parseQueryString(backArg);
            _.extend(params, qstringParams);
        }

        var postData = $.cookie("postData");
        if (postData) {
            _.extend(params, JSON.parse(postData));
        }

        args.push(params);

        return args;
    }


});

var Template = {
    load: function(template, data, selector, append) {
        var baseUrl = "/assets/html/";
        $.when($.get(baseUrl + template + ".html")).done(function(tmplData) {
            var html = _.template(tmplData, data);
            if (append) {
                $(selector).html($(selector).html() + html);
            } else {
                $(selector).html(html);
            }
        });
    }
}

$(document).ready(function() {
    app = new Application();
    Template.load("sidebar", {}, "#sidebar");

    Backbone.history.start();

    //Network.debug = 1;

    //app.navigate("");
});