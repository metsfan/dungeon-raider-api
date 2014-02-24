var ClassSpellEditView = Backbone.View.extend({

    initialize: function(args) {
        this.spell = args.spell
        this.classData = args.classData
    },

    render: function() {
        if (this.spell.char_class) {
            this.classData = this.spell.char_class;
        }

        if (!this.classData) {
            app.navigate("spells", {trigger: true});
        }

        var data = {
            "spell" : this.spell,
            "classData" : this.classData
        }

        Template.load("spell", data, "#content", {
            callback: $.proxy(function() {
                if (this.spell) {
                    $("#spell_id").val(this.spell.id);
                    $("#name").val(this.spell.name);
                    $("#cast_time").val(this.spell.cast_time * 0.001);
                    $("#cooldown").val(this.spell.cooldown * 0.001);
                    $("#spell_type").val(this.spell.spell_type);
                    $("#cast_type").val(this.spell.cast_type);
                    $("#range").val(this.spell.range);
                    $("#radius").val(this.spell.radius);
                    $("#shape").val(this.spell.shape);
                    $("#self_cast").prop("checked", this.spell.self_cast);

                    for (var i = 0; i < this.spell.effects; i++) {
                        var effect = this.spell.effects[i];
                        Template.load("spell_effect", { "index" : i }, "#spell-effects", true);

                        $("#effect.id-" + i).val(effect.id);
                        $("#effect.min_value-" + i).val(effect.min_value);
                        $("#effect.max_value-" + i).val(effect.max_value);
                        $("#effect.duration-" + i).val(effect.duration);
                        $("#effect.stat-" + i).val(effect.stat);
                        $("#effect.effect-" + i).val(effect.effect);
                        $("#effect.school-" + i).val(effect.school);
                    }

                    for (var i = 0; i < this.spell.triggers; i++) {
                        var trigger = this.spell.triggers[i];
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

                $("#spell-submit").on("click", $.proxy(function() {
                    var spell = {
                        "id" : $("#spell_id").val(),
                        "name" : $("#name").val(),
                        "cast_time" : parseFloat($("#cast_time").val()) * 1000,
                        "cooldown" : parseFloat($("#cooldown").val()) * 1000,
                        "spell_type" : parseInt($("#spell_type").val()),
                        "cast_type" : parseInt($("#cast_type").val()),
                        "range" : parseFloat($("#range").val()),
                        "radius" : parseFloat($("#radius").val()),
                        "shape" : parseInt($("#shape").val()),
                        "self_cast" : $("#self_cast").prop("checked"),
                        "effects" : [],
                        "triggers" : [],
                        "class" : this.classData
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

                    this.spell = new Spell(spell);

                    this.spell.save();
                }, this))
            }, this)
        }, this)




    }
});