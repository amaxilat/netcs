// Read a page's GET URL variables and return them as an associative array.
function getUrlVars() {
    var vars = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
    return vars;
}
