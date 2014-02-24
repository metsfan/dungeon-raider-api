var Object = function() {
    this.initialize.apply(this, arguments)
}

_.extend(Object.prototype, {
});

Object.extend = Backbone.Model.extend