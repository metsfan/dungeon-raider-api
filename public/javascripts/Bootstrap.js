var classes = [

    // Util
    "util/Loader.js",
    "util/Util.js",
    "util/Network.js",
    "util/Template.js",

    // Models
    "models/CharClass.js",
    "models/Spell.js",

    // Views
    "views/ClassSelectView.js",
    "views/ClassSpellEditView.js",
    "views/ClassSpellSelectView.js",

    // Controllers
    "controllers/BaseController.js",
    "controllers/SpellController.js",

    //Core
    "Application.js",
];

var scriptHTML = "";
var baseURL = "/assets/javascripts/";
var head = document.getElementsByTagName("head")[0];

for (var i = 0; i < classes.length; i++) {
    var scriptElement = document.createElement("script");
    scriptElement.setAttribute("type", "text/javascript");
    scriptElement.setAttribute("src", baseURL + classes[i]);
    scriptElement.setAttribute("async", false);
    head.appendChild(scriptElement);
}