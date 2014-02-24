/**
Spell Controller.

Allows the editing of player spells.
*/

var SpellController = BaseController.extend({

    classes: new CharClassCollection(),

    index: function() {
        Loader.show()
        $.when(this.loadClasses()).then($.proxy(function() {
            this.render("ClassSelectView", this.classes);
            Loader.hide()
        }, this))
    },

    classIndex: function(classId) {
        var reqs = []

        reqs.push(this.loadClasses());

        var spells = null

        reqs.push(Network.get("spells/" + classId, function(data){
            spells = data
        }))

        Loader.show()
        $.when.apply($, reqs).then($.proxy(function() {
            this.render("ClassSpellSelectView", {
                "classData" : this.classes.get(classId),
                "spells" : spells
            });
            Loader.hide()
        }, this))
    },

    editSpell: function(id, args) {
        if (id > 0) {
            var spell = new Spell({"id" : id})

            Loader.show()
            $.when(spell.fetch()).then($.proxy(function(spell) {
                this.render("ClassSpellEditView", {
                    "spell" : spell,
                    "classData" : args.classData
                });

                Loader.hide()
            }, this))
        }
    },

    createSpell: function(args) {
        var view = new ClassSpellEditView({});
        view.classData = args.classData;
        view.render();
    },

    // Private

    loadClasses: function() {
        return this.classes.fetch();
    }
});

