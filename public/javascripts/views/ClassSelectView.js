var ClassSelectView = Backbone.View.extend({

    initialize: function(classes) {
        Backbone.View.prototype.initialize.call(this);
        this.classes = classes;
    },

    render: function() {
        var data = { "classes" : this.classes }

        Template.load("classes", data, "#content");
    }

});