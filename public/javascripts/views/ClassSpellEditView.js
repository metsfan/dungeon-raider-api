var ClassSpellEditView = Backbone.View.extend({

    initialize: function(spell) {
        this.spell = spell;
    },

    render: function() {
        if (!this.classData) {
            app.navigate("spells", {trigger: true});
        }

        var data = {
            "spell" : this.spell,
            "classData" : this.classData
        }

        Template.load("spell", data, "#content");
    }
});