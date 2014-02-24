var Template = {
    load: function(template, data, selector, options) {
        if (!options) {
            options = {}
        }

        var baseUrl = "/assets/html/";
        $.when($.get(baseUrl + template + ".html")).done(function(tmplData) {
            var html = _.template(tmplData, data);
            var elem = $(selector)
            if (options.append) {
                elem.html(elem.html() + html);
            } else {
                elem.html(html);
            }

            if (options.callback) {
                options.callback()
            }
        });
    }
}