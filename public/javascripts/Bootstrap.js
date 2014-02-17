var classes = [
    // Util
    "util/Loader.js",
    "util/Util.js",

    // Models
    "models/CharacterClass.js",
    "models/Spell.js",

    // Views
    "views/ClassSelectView.js",
    "views/ClassSpellEditView.js",
    "views/ClassSpellSelectView.js",

    // Controllers
    "controllers/BaseController.js",
    "controllers/SpellController.js",

    //Core
    "Network.js",
    "Application.js",
];

var scriptHTML = "";
var baseURL = "/assets/javascripts/";
var head = document.getElementsByTagName("head")[0];

for (var i = 0; i < classes.length; i++) {
    var scriptElement = document.createElement("script");
    scriptElement.setAttribute("type", "text/javascript");
    scriptElement.setAttribute("src", baseURL + classes[i]);
    head.appendChild(scriptElement);
}