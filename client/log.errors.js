logError = function(details) {
    console.error(details);
    Meteor.call('logError', {
        context: navigator.userAgent,
        details: details
    });
};

Meteor.startup(function() {
    window.onerror = function(message, file, line) {
        logError({file: file, line: line, message: message});
    };
    $(document).ajaxError(function(e, xhr, settings) {
        logError({url: settings.url, status: xhr.status, response: xhr.responseText});
    });
});
