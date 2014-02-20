var ClassSpellSelectView = Backbone.View.extend({

    events: {
        "click .add-spell-button": "addSpell"
    },

    initialize: function(classData, spells) {
        this.delegateEvents(this.events);
        this.spells = spells;
        this.classData = classData;
    },

    render: function() {
        var data = {
            "classData" : this.classData,
            "spells" : this.spells
        }
        Template.load("spells", data, "#content");

        $(document).on("click", "#add-spell-button", $.proxy(this.addSpell, this));
    },

    addSpell: function() {
        var options = {
            trigger: true,
            args: {
                "classData" : this.classData.attributes
            }
        }
        app.navigate("spell", options);
    }
});