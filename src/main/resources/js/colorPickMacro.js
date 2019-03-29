const baseUrl = AJS.params.baseUrl;
const pageID = AJS.params.pageId;

function updateImage(file, uniqueFilename) {
    console.log(file);
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
            var attID = lastImage.id;
            var link = lastImage._links.download;
            var fullpath = baseUrl + link;
            uploadDB(fullpath, attID)
        },
        error: function () {
            AJS.messages.error("#error-messages",{
                title: 'Error',
                body: '<p>Error occurred while creating attachment</p>'
            })
        }
    })
}

function uploadDB(fullpath, attID) {
    AJS.$.ajax({
        url: baseUrl + '/rest/restresource/1.0/attach/' + pageID,
        type: "POST",
        headers: {
            "attID": attID,
            "path": fullpath,
            "X-Atlassian-Token": "nocheck"
        },
        success: function () {
            setBackgroundImage(fullpath);
        },
        error: function () {
            AJS.messages.error("#error-messages",{
                title: 'Error',
                body: '<p>Error occurred while uploading database</p>'
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
            404: function() {
                AJS.messages.info("#error-messages",{
                    title: 'Note!',
                    body: '<p>You can attach image by clicking "Upload a file" button</p>'

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
            if (content.attID === "none") {
                generateUniqueFilname(input)
            } else {
                updateAttachment(content.attID, file);
            }
        },
        error: function () {
            AJS.messages.error("#error-messages",{
                title: 'Error',
                body: '<p>Error occurred while checking user attachments </p>'
            })
        }

    })
}

function updateAttachment(attID, file) {
    console.log(file);
    var actionData = new FormData();
    actionData.append('file', file);
    AJS.$.ajax({
        url: baseUrl + '/rest/api/content/' + pageID + '/child/attachment/' + attID + "/data",
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
            uploadDB(fullpath, attID);
        },
        error: function () {
            AJS.messages.error("#error-messages",{
                title: 'Error',
                body: '<p>Error occurred while uploading attachment data </p>'
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
            AJS.messages.error("#error-messages",{
                title: 'Error',
                body: '<p>Error occurred while generated unique filename</p>'
            })
        }
    })
}

function setBackgroundImage(path) {
    $('#main').append('<div class="user-background-image">');
    $('.user-background-image').css({"background-image": "url(" + path + ')'});
}