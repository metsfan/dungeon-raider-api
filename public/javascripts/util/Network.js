var Network = {
    debug: 0,

    get: function(url, success, error) {
        return Network.send(url, "GET", {
            "success" : success,
            "error" : error
        })
    },

    send: function(url, method, options) {
        Loader.show();

        var success = options.success;
        var error = options.error

        return $.ajax(_.extend(options, {
            "url" : url,
            "type" : method,
            "success" : function(data) {
                if (success) {
                    success(data);
                }

                Loader.hide();
            },
            "error" : function(xhr, status, e) {
                if (error) {
                    error(e);
                }

                if (options.debug) {
                    console.log(e);
                }

                Loader.hide();
            }
        }))
    }
}