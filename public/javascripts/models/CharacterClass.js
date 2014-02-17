var CharacterClass = Backbone.Model.extend({

});

var CharacterClassCollection = Backbone.Collection.extend({
    model: function(attrs, options) {
        return new CharacterClass(attrs);
    }
});