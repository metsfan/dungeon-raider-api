var CharClass = Backbone.Model.extend({

});

var CharClassCollection = Backbone.Collection.extend({
    model: CharClass,

    url: "/classes"
});