function websAjaxLoad(fetchId, ajaxDataHandler) {

    $(fetchId).click(function(event) {

        $.ajax({
            url: $(event.target).attr("href"),
            type: "GET",

            beforeSend: function(xhr) {
                xhr.setRequestHeader("Accept", "application/json");
                xhr.setRequestHeader("Content-Type", "application/json");
            },

            success: function(data) {
                ajaxDataHandler(data);
            }
        });

        event.preventDefault();
    });
}

function saveFile(data) {
    saveAs(new Blob(data.content, {type: "text/plain;charset=utf-8"}), data.filename);
}

function websAjaxSaveFile(linkId) {
    websAjaxLoad(linkId, saveFile);
}
