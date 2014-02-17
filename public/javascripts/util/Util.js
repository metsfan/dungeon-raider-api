var Util = {

    parseQueryString: function(query) {
        var params = query.split("&");

        var outParams = {};

        _.each(params, function(param) {
            var parts = param.split("=");
            outParams[parts[0]] = parts[1];
        });

        return outParams;
    }
}