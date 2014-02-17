var BaseController = {
    render: function(view, data) {
        var view = new view();
        view.render(data);
    },

    extend: function(options) {
        return _.extend(options, this);
    }
}
