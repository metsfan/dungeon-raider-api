var BaseController = Backbone.Model.extend({
    render: function(view, data) {
        var args = Array.prototype.slice.call(arguments, 1)
        var view = eval("new " + view + "(data)")
        view.render();
    }
})
