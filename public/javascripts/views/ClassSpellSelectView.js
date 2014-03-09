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
        var coreSpells = this.spells.filter(function(spell) {
            return spell.attributes.slot &&
            spell.attributes.slot.search("s") >= 0
        })

        coreSpells = _.sortBy(coreSpells, function(spell) {
            return parseInt(spell.attributes.slot.replace("s", ""));
        })

        var cooldownSpells = this.spells.filter(function(spell) {
            return spell.attributes.slot &&
            spell.attributes.slot.search("cd") >= 0
        })

        cooldownSpells = _.sortBy(cooldownSpells, function(spell) {
            return parseInt(spell.attributes.slot.replace("cd", ""))
        })

        var uncategorizedSpells = this.spells.filter(function(spell) {
            return !spell.attributes.slot
        })

        var data = {
            "classData" : this.classData,
            "spells" : [
                {
                    "list" : coreSpells,
                    "name" : "core",
                    "title" : "Core",
                    "type" : "s"
                },
                {
                    "list" : cooldownSpells,
                    "name" : "cooldown",
                    "title" : "Cooldown",
                    "type" : "cd"
                },
                {
                    "list" : uncategorizedSpells,
                    "name" : "uncategorized",
                    "title" : "Uncategorized",
                    "type" : ""
                },
            ]
        }
        Template.load("spells", data, "#content", {
            callback: $.proxy(function() {
                _.each(data.spells, function(spells) {
                    $("#spells-table-" + spells.name + " .spell-category").val(spells.name)
                })

                this.refreshTableDnD();

                $(".spell-category").change($.proxy(function(e) {
                    var elem = $(e.target)
                    var value = elem.val()
                    var parentRow = elem.parents("tr")
                    var spellId = parseInt(parentRow.attr("data-spell-id"))

                    var curSpell = this.spells.find(function(spell) {
                        return spell.id == spellId
                    })

                    if (curSpell) {
                        var slot = ""
                        var index = $("#spells-table-" + value + " tbody tr").length

                        if (value == "core") {
                            slot = "s" + index
                        } else if (value == "cooldown") {
                            slot = "cd" + index
                        }

                        curSpell.slot = slot
                        parentRow.attr("data-slot", slot)

                        $("#spells-table-" + value).append(parentRow)
                    }

                    this.refreshTableDnD();
                }, this))

                $("#update-spells-button").click($.proxy(function() {
                    var data = this.spells.map(function(spell) {
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

                $(".spell-delete").click($.proxy(function(e) {
                    var el = $(e.target)
                    var spellId = $(e.target).attr("data-spell-id")
                    var spell = this.spells.get(spellId)

                    var shouldDelete = confirm("Are you sure you want to delete " + spell.attributes.name + "?")
                    if (shouldDelete) {
                        spell.destroy();
                        this.spells.remove(spell);

                        var parentRow = el.parents("tr");
                        parentRow.remove();
                    }
                }, this));
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
    },

    refreshTableDnD: function() {
        $(".spells-table[type!='']").tableDnD({
            onDrop: function(table, row) {
                var tableEl = $(table);
                var type = tableEl.attr("type");

                tableEl.find(".spell-row").each(function(i, elem) {
                    $(elem).attr("data-slot", type + i);
                });
            }
        });
    }
});