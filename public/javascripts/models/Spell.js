var Spell = Backbone.Model.extend({
    urlRoot: "/spell",
});

var SpellCollection = Backbone.Collection.extend({
    model: Spell,

    url: "spells"
});

