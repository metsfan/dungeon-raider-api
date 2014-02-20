var ClassSpellEditView = Backbone.View.extend({

    initialize: function(spell) {
        this.spell = spell;
    },

    render: function() {
        if (!this.classData) {
            app.navigate("spells", {trigger: true});
        }

        var data = {
            "spell" : this.spell,
            "classData" : this.classData
        }

        Template.load("spell", data, "#content");

        if (this.spell.attributes) {
            $("#spell_id").val(this.spell.attributes.id);
            $("#name").val(this.spell.attributes.name);
            $("#cast_time").val(this.spell.attributes.cast_time);
            $("#cooldown").val(this.spell.attributes.cooldown);
            $("#spell_type").val(this.spell.attributes.spell_type);
            $("#cast_type").val(this.spell.attributes.cast_type);
            $("#range").val(this.spell.attributes.range);
            $("#radius").val(this.spell.attributes.radius);
            $("#shape").val(this.spell.attributes.shape);

            for (var i = 0; i < this.spell.attributes.effects; i++) {
                var effect = this.spell.attributes.effects[i];
                Template.load("spell_effect", { "index" : i }, "#spell-effects", true);

                $("#effect.id-" + i).val(effect.id);
                $("#effect.min_value-" + i).val(effect.min_value);
                $("#effect.max_value-" + i).val(effect.max_value);
                $("#effect.duration-" + i).val(effect.duration);
                $("#effect.stat-" + i).val(effect.stat);
                $("#effect.effect-" + i).val(effect.effect);
                $("#effect.school-" + i).val(effect.school);
            }

            for (var i = 0; i < this.spell.attributes.triggers; i++) {
                var trigger = this.spell.attributes.triggers[i];
                Template.load("spell_trigger", { "index" : i }, "#spell-triggers", true);

                $("#trigger.id-" + i).val(trigger.id);
                $("#trigger.trigger_spell_id-" + i).val(trigger.trigger_spell_id);
                $("#trigger.trigger_type-" + i).val(trigger.trigger_type);
                $("#trigger.chance-" + i).val(trigger.chance);
            }
        }

        $("#add-effect-button").on("click", function() {
            var i = $("#spell-effects").children().length;
            Template.load("spell_effect", { "index" : i }, "#spell-effects", true);
        });

        $("#add-trigger-button").on("click", function() {
            var i = $("#spell-triggers").children().length;
            Template.load("spell_trigger", { "index" : i }, "#spell-triggers", true);
        });

        $("#spell-submit").on("click", function() {
            var spell = {
                "id" : $("#spell_id").val(),
                "name" : $("#name").val(),
                "cast_time" : $("#cast_time").val();
                "cooldown" : $("#cooldown").val(),
                "spell_type" : $("#spell_type").val(),
                "cast_type" : $("#cast_type").val(),
                "range" : $("#range").val(),
                "radius" : $("#radius").val(),
                "shape" : $("#shape").val(),
                "effects" : [],
                "triggers" : []
            };

            $(".spell-effect-container").each(function(i, elem) {
                spell.effects.push({
                    "id" : $("#effect.id-" + i).val(),
                    "min_value" : $("#effect.min_value-" + i).val(),
                    "max_value" : $("#effect.max_value-" + i).val(),
                    "duration" : $("#effect.duration-" + i).val(),
                    "stat" : $("#effect.stat-" + i).val(),
                    "effect" : $("#effect.effect-" + i).val(),
                    "school" : $("#effect.school-" + i).val()
                });
            });

            $(".spell-trigger-container").each(function(i, elem) {
                spell.effects.push({
                    "id" : $("#trigger.id-" + i).val(),
                    "trigger_spell_id" : $("#trigger-trigger_spell_id-" + i).val(),
                    "trigger_type" : $("#trigger.trigger_type-" + i).val(),
                    "chance" : $("#trigger.chance-" + i).val(),
                });
            });
        });
    }
});