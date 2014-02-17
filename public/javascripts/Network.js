var Network = {
    debug: 0,

    get: function(url, success, failure) {
        Loader.show();

        $.ajax({
            "url" : url,
            "success" : function(data) {
                if (success) {
                    success(data);
                }

                Loader.hide();
            },
            "error" : function(xhr, status, error) {
                if (failure) {
                    failure(error);
                }

                if (Network.debug) {
                    console.log(error);
                }

                Loader.hide();
            }
        });
    }
}