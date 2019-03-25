const baseUrl = AJS.params.baseUrl;
const pageID = AJS.params.pageId;

function updateImage(file, uniquefilename) {
    file.name = uniquefilename;
    console.log(file);
    var actionData = new FormData();
    actionData.append('file', file, uniquefilename);
    actionData.append('comment', "foobar");
    actionData.append('minorEdit', "true");
    AJS.$.ajax({
        url: baseUrl + '/rest/api/content/' + pageID + '/child/attachment',
        type: "POST",
        data: actionData,
        dataType: "json",
        processData: false,
        headers: {
            "X-Atlassian-Token": "nocheck",
        },
        contentType: false,
        cache: false,
        success: [function (content) {
            alldata = content.results;
            lastImage = alldata[alldata.length - 1];
            link = lastImage._links.download;
            fullpath = baseUrl + link;
            setBackgroundImage(fullpath);
            uploadDB(fullpath)
        }],
        error: [function () {
            console.log("error while creating attachment")
        }]
    })
}

function uploadDB(fullpath) {
    AJS.$.ajax({
        url: baseUrl + '/rest/restresource/1.0/attach/' + pageID,
        type: "POST",
        headers: {
            "path": fullpath,
            "X-Atlassian-Token": "nocheck",
        },
        success: [function () {
        }],
        error: [[function () {
            console.log("error while upload information")
        }]]
    })
}

AJS.toInit(function getImage() {
    AJS.$.ajax({
        url: baseUrl + '/rest/restresource/1.0/attach/' + pageID,
        type: "GET",
        dataType: "json",
        success: [function (content) {
            setBackgroundImage(content.path)
        }],
        error: [function () {
            console.log("error while downloading image")
        }]

    })
});

function generateUniqueFilname(input) {
    file = input.files[0];
    filename = input.files[0].name;
    Extension = filename.split('.').pop();
    AJS.$.ajax({
        url: baseUrl + '/rest/restresource/1.0/attach',
        headers:{
          "filename": file,
        },
        type: "GET",
        dataType: "text",
        success: [function (uniqueFilename) {
            uniqueFilename += '.' + Extension;
            updateImage(file, uniqueFilename);
        }],
        error: [function () {
                console.log("error while generating filename")
        }]
    })
}

function setBackgroundImage(path) {
    $('#main').append('<div class="background-image">');
    $('.background-image').css({"background-image": "url(" + path + ')'});
}