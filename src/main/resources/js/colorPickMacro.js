const baseUrl = AJS.params.baseUrl;
const pageID = AJS.params.pageId;

function updateImage(file, uniqueFilename) {
    var actionData = new FormData();
    actionData.append('file', file, uniqueFilename);
    actionData.append('comment', "foobar");
    actionData.append('minorEdit', "true");
    AJS.$.ajax({
        url: baseUrl + '/rest/api/content/' + pageID + '/child/attachment',
        type: "POST",
        data: actionData,
        dataType: "json",
        processData: false,
        headers: {
            "X-Atlassian-Token": "nocheck"
        },
        contentType: false,
        cache: false,
        success: function (content) {
            var alldata = content.results;
            var lastImage = alldata[alldata.length - 1];
            var attachmentId = lastImage.id;
            var link = lastImage._links.download;
            var fullpath = baseUrl + link;
            uploadDB(fullpath, attachmentId)
        },
        error: function () {
            AJS.messages.error("#error-messages", {
                title: AJS.I18n.getText('message.error'),
                body: AJS.I18n.getText('message.error-while-creating-attachment')
            })
        }
    })
}

function uploadDB(fullpath, attachmentId) {
    AJS.$.ajax({
        url: baseUrl + '/rest/restresource/1.0/attach/' + pageID,
        type: "POST",
        headers: {
            "attachmentId": attachmentId,
            "path": fullpath,
            "X-Atlassian-Token": "nocheck"
        },
        success: function () {
            setBackgroundImage(fullpath);
        },
        error: function () {
            AJS.messages.error("#error-messages", {
                title: AJS.I18n.getText('message.error'),
                body: AJS.I18n.getText('message.error-while-uploading-database'),
            })
        }
    })
}

AJS.toInit(function getImage() {
    AJS.$.ajax({
        url: baseUrl + '/rest/restresource/1.0/attach/' + pageID,
        type: "GET",
        dataType: "json",
        statusCode: {
            404: function () {
                AJS.messages.info("#error-messages", {
                    title: AJS.I18n.getText('message.note'),
                    body: AJS.I18n.getText('message.advice')

                })
            }
        },
        success: function (content) {
            setBackgroundImage(content.path)
        }
    })
});

function checkUserAttachments(input) {
    var file = input.files[0];
    AJS.$.ajax({
        url: baseUrl + '/rest/restresource/1.0/attach/' + pageID,
        type: "GET",
        dataType: "json",
        success: function (content) {
            if (content.attachmentId === "none") {
                generateUniqueFilname(input)
            } else {
                updateAttachment(content.attachmentId, file);
            }
        },
        error: function () {
            AJS.messages.error("#error-messages", {
                title: AJS.I18n.getText('message.error'),
                body: AJS.I18n.getText('message.error-while-checking-attachments')
            })
        }
    })
}

function updateAttachment(attachmentId, file) {
    console.log(file);
    var actionData = new FormData();
    actionData.append('file', file);
    AJS.$.ajax({
        url: baseUrl + '/rest/api/content/' + pageID + '/child/attachment/' + attachmentId + "/data",
        type: "POST",
        data: actionData,
        dataType: "json",
        processData: false,
        headers: {
            "X-Atlassian-Token": "nocheck"
        },
        contentType: false,
        cache: false,
        success: function (content) {
            var link = content._links.download;
            var fullpath = baseUrl + link;
            uploadDB(fullpath, attachmentId);
        },
        error: function () {
            AJS.messages.error("#error-messages", {
                title: AJS.I18n.getText('message.error'),
                body: AJS.I18n.getText('message.error-while-uploading-attachments')
            })
        }
    })

}

function generateUniqueFilname(input) {
    var file = input.files[0];
    var filename = input.files[0].name;
    var extension = filename.split('.').pop();
    AJS.$.ajax({
        url: baseUrl + '/rest/restresource/1.0/attach',
        headers: {
            "filename": filename
        },
        type: "GET",
        dataType: "text",
        success: function (uniqueFilename) {
            uniqueFilename += '.' + extension;
            updateImage(file, uniqueFilename);
        },
        error: function () {
            AJS.messages.error("#error-messages", {
                title: AJS.I18n.getText('message.error'),
                body: AJS.I18n.getText('message.error-while-generated-filename')
            })
        }
    })
}

function setBackgroundImage(path) {
    $('#main').append('<div class="user-background-image">');
    $('.user-background-image').css({"background-image": "url(" + path + ')'});
}