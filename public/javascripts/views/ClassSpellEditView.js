var EffectType = {
    Damage: 0,
    Healing: 1,
    DamageOverTime: 2,
    HealingOverTime: 3,
    Buff: 4,
    Debuff: 5,
    Mechanic: 6,
    Script: 7
}

var ClassSpellEditView = Backbone.View.extend({

    fields: [
        ["damage_source", "percent_source", "flat_amount", "school"], // Damage
        ["damage_source", "percent_source", "flat_amount", "school"], // Healing
        ["damage_source", "dot_duration", "percent_source", "flat_amount", "max_stacks", "school"], // DamageOverTime
        ["damage_source", "dot_duration", "percent_source", "flat_amount", "max_stacks", "school"], // HealingOverTime
        ["buff_source", "duration", "percent_source", "flat_amount", "max_stacks", "school"], // Buff
        ["buff_source", "duration", "percent_source", "flat_amount", "max_stacks", "school"], // Debuff
        ["mechanic", "duration", "percent_source", "flat_amount", "school"], // Mechanic
        ["script_name", "script_arguments"] // Script
    ],

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
                if (!this.imageUploader) {
                    this.imageUploader = new ImageUploadView({
                        "selector" : "#spell-icon-wrapper",
                        "url" : "/upload/image",
                        "image" : this.spell.icon_url,
                        "no_button" : true
                    });
                }

                $(document).on("change", ".effect_type", $.proxy(function(e) {
                    this._showEffectFields($(e.target))
                }, this))

                $(document).on("click", ".effect-delete", $.proxy(function(e) {
                    var accept = confirm("Are you sure you want to delete this effect?");

                    if (accept) {
                        var button = $(e.target)
                        var id = parseInt(button.attr("data-id"))
                        if (id > 0) {
                            var url = "/spell/effect/" + id
                            Network.send(url, "DELETE", {});
                        }

                        button.parents(".spell-effect-container").remove()
                    }

                    return false
                }, this));

                $(document).on("click", ".trigger-delete", $.proxy(function(e) {
                    var accept = confirm("Are you sure you want to delete this trigger?");

                    if (accept) {
                        var button = $(e.target)
                        var id = parseInt(button.attr("data-id"))
                        if (id > 0) {
                            var url = "/spell/trigger/" + id
                            Network.send(url, "DELETE", {});
                        }

                        button.parents(".spell-trigger-container").remove()
                    }

                    return false
                }, this));

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

                    if (this.spell.effects && this.spell.effects.length > 0) {
                        this._loadEffects(this.spell.effects)
                    }

                    $("#spell-effects").sortable({
                        "stop" : function() {
                            $(this).children().each(function(i, elem) {
                                $(this).find(".spell-index").html(i+1)
                            })
                        }
                    })

                    if (this.spell.triggers && this.spell.triggers.length > 0) {
                        this._loadTriggers(this.spell.triggers)
                    }
                }

                $("#add-effect-button").on("click", function() {
                    var i = $("#spell-effects").children().length;
                    Template.load("spell_effect", { "index" : i, "id" : 0 }, "#spell-effects", {
                        "append" : true,
                        "callback" : function() {
                            $("#effect-effect_type-" + i).change()
                        }
                    });
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
                        "char_class" : this.classData,
                        "icon_url" : this.imageUploader.getImageURL()
                    };

                    $(".spell-effect-container").each(function(i, elem) {
                        var index = parseInt($(elem).attr("data-index"))
                        spell.effects.push({
                            "id" : parseInt($("#effect-id-" + index).val()),
                            "spell_id" : parseInt(spell.id),
                            "effect_type" : parseInt($("#effect-effect_type-" + index).val()),
                            "damage_source" : parseInt($("#effect-damage_source-" + index).val()),
                            "buff_source" : parseInt($("#effect-buff_source-" + index).val()),
                            "percent_source_min" : parseInt($("#effect-percent_source_min-" + index).val()),
                            "percent_source_max" : parseInt($("#effect-percent_source_max-" + index).val()),
                            "flat_amount_min" : parseInt($("#effect-flat_amount_min-" + index).val()),
                            "flat_amount_max" : parseInt($("#effect-flat_amount_max-" + index).val()),
                            "dot_tick" : parseInt($("#effect-dot_tick-" + index).val()),
                            "dot_duration" : parseInt($("#effect-dot_duration-" + index).val()),
                            "buff_duration" : parseInt($("#effect-buff_duration-" + index).val()),
                            "mechanic" : parseInt($("#effect-mechanic-" + index).val()),
                            "school" : parseInt($("#effect-school-" + index).val()),
                            "script_name" : $("#effect-script_name-" + index).val(),
                            "script_arguments" : $("#effect-script_arguments-" + index).val(),
                            "delta" : parseInt(i),
                            "max_stacks": parseInt($("#effect-max_stacks-" + index).val())
                        });
                    });

                    $(".spell-trigger-container").each(function(i, elem) {
                        spell.triggers.push({
                            "id" : $("#trigger.id-" + i).val(),
                            "trigger_spell_id" : $("#trigger-trigger_spell_id-" + i).val(),
                            "trigger_type" : $("#trigger.trigger_type-" + i).val(),
                            "chance" : $("#trigger.chance-" + i).val(),
                        });
                    });
                    this.spell = new Spell(spell);

                    Loader.show()
                    $.when(this.spell.save()).then($.proxy(function(data) {
                        app.navigate("spell/" + data.id, {trigger: true})
                        alert("Save Complete")

                        Loader.hide()
                    }, this));
                }, this))
            }, this)
        }, this)
    },

    _loadEffects: function(effects, i) {
        if (!i) i = 0

        var effect = effects[i]
        Template.load("spell_effect", { "index" : i, "id": effect.id }, "#spell-effects", {
            "append" : true,
            "callback" : $.proxy(function() {
                $("#effect-id-" + i).val(effect.id),
                $("#effect-effect_type-" + i).val(effect.effect_type),
                $("#effect-damage_source-" + i).val(effect.damage_source),
                $("#effect-buff_source-" + i).val(effect.buff_source),
                $("#effect-percent_source_min-" + i).val(effect.percent_source_min),
                $("#effect-percent_source_max-" + i).val(effect.percent_source_max),
                $("#effect-flat_amount_min-" + i).val(effect.flat_amount_min),
                $("#effect-flat_amount_max-" + i).val(effect.flat_amount_max),
                $("#effect-dot_tick-" + i).val(effect.dot_tick),
                $("#effect-dot_duration-" + i).val(effect.dot_duration),
                $("#effect-buff_duration-" + i).val(effect.buff_duration),
                $("#effect-mechanic-" + i).val(effect.mechanic),
                $("#effect-school-" + i).val(effect.school),
                $("#effect-script_name-" + i).val(effect.script_name),
                $("#effect-script_arguments-" + i).val(effect.script_arguments)
                $("#effect-max_stacks-" + i).val(effect.max_stacks)
                $("#effect-effect_type-" + i).change()

                if (effects.length > i + 1) {
                    this._loadEffects(effects, i + 1)
                }
            }, this)
        })
    },

    _loadTriggers: function(triggers, i) {
        if (!i) i = 0

        var trigger = this.spell.triggers[i];
        Template.load("spell_trigger", { "index" : i, "id": trigger.id }, "#spell-triggers", {
            "append" : true,
            "callback" : function() {
                $("#trigger-id-" + i).val(trigger.id);
                $("#trigger-trigger_spell_id-" + i).val(trigger.trigger_spell_id);
                $("#trigger-trigger_type-" + i).val(trigger.trigger_type);
                $("#trigger-chance-" + i).val(trigger.chance);

                if (triggers.length > i + 1) {
                    this._loadTriggers(triggers, i + 1)
                }
            }
        });
    },

    _showEffectFields: function(elem) {
        var type = parseInt(elem.val())
        var root = elem.parents(".spell-effect-container")

        root.find(".form-element-container").hide()

        root.find(".effect_type-container").show()

        var fields = this.fields[type]

        for (var i = 0; i < fields.length; i++) {
            root.find("." + fields[i] + "-container").show()
        }
    }
});