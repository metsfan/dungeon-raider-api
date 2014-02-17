var ClassSpellEditView = Backbone.View.extend({

    initialize: function(spell) {
        this.spell = spell;
    },

    render: function() {
        var data = {
            "spell" : this.spell
        }

        Template.load("spell", data, "#content");
    }
});