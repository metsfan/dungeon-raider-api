var ImageUploadView = Backbone.View.extend({

    initialize: function(options) {
        this.options = options;

        this.render();
    },

    render: function() {
        Template.load("image_upload", {}, this.options.selector, {
            "callback" : $.proxy(function() {
                this.elem = $(this.options.selector);

                this.activeImage = this.elem.find(".active-image");
                this.fileUpload = this.elem.find(".image-file-upload");
                this.uploadButton = this.elem.find(".image-upload-button");
                this.uploadForm = this.elem.find(".image-file-upload-form");

                if (this.options.image) {
                    this.activeImage.attr("src", this.options.image)
                }

                if (this.options.no_button) {
                    this.uploadButton.hide()

                    this.fileUpload.change($.proxy(function() {
                        this.uploadForm.submit()
                    }, this))
                }

                this.uploadForm.submit($.proxy(function() {
                    var formData = new FormData(this.uploadForm[0])
                    Network.send(this.options.url, "POST", {
                        data: formData,
                        success: $.proxy(function(data) {
                            this.activeImage.attr("src", data.imageURL)
                        }, this),
                        cache: false,
                        contentType: false,
                        processData: false
                    })

                    return false;
                }, this));
            }, this)
        });
    },

    getImageURL: function() {
        return this.activeImage.attr("src")
    }

});