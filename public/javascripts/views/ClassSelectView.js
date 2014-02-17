var ClassSelectView = Backbone.View.extend({

    initialize: function(classes) {
        Backbone.View.prototype.initialize.call(this);
        this.classes = classes;
    },

    render: function() {
        Template.load("classes", {"classes" : this.classes}, "#content");
    }
});