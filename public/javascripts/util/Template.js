var Template = {
    load: function(template, data, selector, options) {
        if (!options) {
            options = {}
        }

        var baseUrl = "/assets/html/";
        return $.when($.get(baseUrl + template + ".html")).done(function(tmplData) {
            var html = _.template(tmplData, data);
            var elem = $(selector)

            var copies = options.copies ? options.copies : 1


            if (options.append) {
                for (var i = 0; i < copies; i++) {
                    elem.append(html);
                }
            } else {
                elem.html(html);
                for (var i = 1; i < copies; i++) {
                    elem.append(html);
                }
            }

            if (options.callback) {
                options.callback()
            }
        });
    }
}