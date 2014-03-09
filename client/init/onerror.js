window.onerror = function(msg, url, line) {
    console.error(msg, url+':'+line);
    // You can view the information in an alert to see things working
    // like so:
    alert("Error: " + msg + "\nurl: " + url + "\nline #: " + line);

    // TODO: Report this error via ajax so you can keep track
    //       of what pages have JS issues

    var suppressErrorAlert = true;
    // If you return true, then error alerts (like in older versions of 
    // Internet Explorer) will be suppressed.
    return suppressErrorAlert;
};