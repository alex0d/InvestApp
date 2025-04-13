addEventListener("DOMContentLoaded", function (e1) {
    e1.originalTarget.addEventListener(
        "framescript:log",
        function (e2) {
            sendAsyncMessage("webview:action", e2.detail)
        }
    );
});
