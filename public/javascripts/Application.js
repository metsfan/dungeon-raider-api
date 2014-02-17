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
        if (args[args.length - 1] != null) {
            var params = Util.parseQueryString(args.pop());
            args.push(params);
        }

        if (callback) {
            callback.apply(this, args);
        }
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