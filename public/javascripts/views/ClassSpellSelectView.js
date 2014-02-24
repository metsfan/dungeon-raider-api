var ClassSpellSelectView = Backbone.View.extend({

    events: {
        "click .add-spell-button": "addSpell"
    },

    initialize: function(arguments) {
        this.delegateEvents(this.events);
        this.spells = arguments.spells;
        this.classData = arguments.classData;

    },

    render: function() {
        var coreSpells = _.filter(this.spells, function(spell) {
            return spell.slot && spell.slot.search("s") >= 0
        }).sort(function(spell) {
            return parseInt(spell.slot.replace("s+", ""))
        })

        var cooldownSpells = _.filter(this.spells, function(spell) {
            return spell.slot && spell.slot.search("c") >= 0
        }).sort(function(spell) {
            return parseInt(spell.slot.replace("c+", ""))
        })

        var uncategorizedSpells = _.filter(this.spells, function(spell) {
            return !spell.slot
        })

        var data = {
            "classData" : this.classData,
            "spells" : [
                {
                    "list" : coreSpells,
                    "name" : "core",
                    "title" : "Core"
                },
                {
                    "list" : cooldownSpells,
                    "name" : "cooldown",
                    "title" : "Cooldown"
                },
                {
                    "list" : uncategorizedSpells,
                    "name" : "uncategorized",
                    "title" : "Uncategorized"
                },
            ]
        }
        Template.load("spells", data, "#content", {
            callback: $.proxy(function() {
                _.each(data.spells, function(spells) {
                    $("#spells-table-" + spells.name + " .spell-category").val(spells.name)
                })

                $(".spell-category").change($.proxy(function(e) {
                    var elem = $(e.target)
                    var value = elem.val()
                    var parentRow = elem.parents("tr")
                    var spellId = parseInt(parentRow.attr("data-spell-id"))

                    var curSpell = _.find(this.spells, function(spell) {
                        return spell.id == spellId
                    })

                    if (curSpell) {
                        var slot = ""
                        var index = $("#spells-table-" + value + " tbody tr").length

                        if (value == "core") {
                            slot = "s+" + index
                        } else if (value == "cooldown") {
                            slot = "c+" + index
                        }

                        curSpell.slot = slot
                        parentRow.attr("data-slot", slot)

                        $("#spells-table-" + value).append(parentRow)
                    }
                }, this))

                $("#update-spells-button").click($.proxy(function() {
                    var data = _.map(this.spells, function(spell) {
                        var slot = $("#spell-row-" + spell.id).attr("data-slot")
                        return {
                            "spell_id" : spell.id,
                            "slot" : slot
                        }
                    })

                    var classId = this.classData.attributes.id
                    var url = "/spells/" + classId + "/slots"
                    Network.send(url, "PUT", {
                        "data" : JSON.stringify(data),
                        "contentType" : "application/json",
                        "success" : function() {
                            alert("Update complete")
                        }
                    })
                }, this))
            }, this)
        });

        $(document).on("click", "#add-spell-button", $.proxy(this.addSpell, this));
    },

    addSpell: function() {
        var options = {
            trigger: true,
            args: {
                "classData" : this.classData.attributes
            }
        }
        app.navigate("spell", options);
    }
});