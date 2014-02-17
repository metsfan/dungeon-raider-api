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

    execute: function(callback, args) {
        var params = {}
        var backArg = args.pop();
        if (backArg != null) {
            var qstringParams = Util.parseQueryString(backArg);
            _.extend(params, qstringParams);
        }

        if (this.postArgs) {
            _.extend(params, this.postArgs);
            this.postArgs = null;
        }

        args.push(params);

        if (callback) {
            callback.apply(this, args);
        }
    },

    navigate: function(fragment, options) {
        this.postArgs = options.args;

        Backbone.Router.prototype.navigate.apply(this, arguments);
    }


});

var Template = {
    load: function(template, data, selector) {
        var baseUrl = "/assets/html/";
        $.when($.get(baseUrl + template + ".html")).done(function(tmplData) {
            var html = _.template(tmplData, data);
            $(selector).html(html);
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