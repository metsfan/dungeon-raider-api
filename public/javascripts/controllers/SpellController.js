/**
Spell Controller.

Allows the editing of player spells.
*/

var SpellController = {

    classes: null,

    index: function() {
        if (SpellController.classes == null) {
            SpellController.loadClasses();
        }

        var view = new ClassSelectView(SpellController.classes);
        view.render();
    },

    classIndex: function(classId) {
        if (SpellController.classes == null) {
            SpellController.loadClasses();
        }

        var classObj = SpellController.classes.get(classId);
        Network.get("spells/" + classId, function(data){
            var view = new ClassSpellSelectView(classObj, data);
            view.render();
        });
    },

    editSpell: function(id, args) {
        if (id > 0) {
            Network.get("/spell/" + id, function(data) {
                var view = new ClassSpellEditView(data);
                view.classData = args.classData;
                view.render();
            });
        }
    },

    createSpell: function(args) {
        var view = new ClassSpellEditView({});
        view.classData = args.classData;
        view.render();
    },

    // Private

    loadClasses: function() {
        SpellController.classes = new CharacterClassCollection([
              {
                  "id" : 0,
                  "name": "Warrior"
              },
              {
                  "id" : 1,
                  "name": "Mage"
              },
              {
                  "id" : 2,
                  "name" : "Hunter"
              }
        ])
    }
};

