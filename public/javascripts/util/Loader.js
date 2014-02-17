var Loader = {
    modalBgElem: null,

    loaderElem: null,

    textElem: null,

    show: function(text) {
        if (this.loaderElem == null) {
            this.modalBgElem = $('<div id="modal_bg"></div>');
            this.modalBgElem.css({
                "position" : "absolute",
                "left" : "0",
                "top" : "0",
                "background-color" : "black",
                "opacity" : "0.5"
            });

            this.loaderElem = $('<img src="/assets/images/ajax-loader.gif">');
            this.loaderElem.css({
                "position" : "absolute"
            });

            //this.textElem = $('<div id="loader-text"></div>');
            //this.loaderElem.append(this.textElem);

            $("body").append(this.modalBgElem);
            $("body").append(this.loaderElem);
        }

        if (!text) {
            text = "";
        }

        //this.textElem.html(text);

        this.modalBgElem.css({
            "width" : $(window).width(),
            "height" : $(window).height()
        });

        this.loaderElem.css({
            "left" : ($(window).width() * 0.5) - (this.loaderElem.width() * 0.5),
            "top" : ($(window).height() * 0.5) - (this.loaderElem.height() * 0.5)
        });

        this.modalBgElem.show();
        this.loaderElem.show();
    },

    hide: function() {
        if (this.modalBgElem != null) {
            this.modalBgElem.hide();
        }

        if (this.loaderElem != null) {
            this.loaderElem.hide();
        }
    }
}