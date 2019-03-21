AJS.$(function () {
    AJS.$('.ffi input[type="file"]').fancyFileInput();
});

function updateImage(file, uniquefilename) {
    file.name = uniquefilename;
    console.log(file);
    var actionData = new FormData();
    actionData.append('file', file, uniquefilename);
    actionData.append('comment', "foobar");
    actionData.append('minorEdit', "true");
    baseUrl = AJS.params.baseUrl;
    pageID = AJS.params.pageId;
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
            $('#main').css({"background-image": "url(" + fullpath + ')'});
            alert(link);
            uploadDB(fullpath)
        }],
        error: [function () {
            alert("fuck")
        }]
    })
}

function uploadDB(fullpath) {
    baseUrl = AJS.params.baseUrl;
    pageID = AJS.params.pageId;
    AJS.$.ajax({
        url: baseUrl + '/rest/myrestresource/1.0/message/' + pageID,
        type: "POST",
        headers: {
            "path": fullpath,
            "X-Atlassian-Token": "nocheck",
        },
        success: [function () {
            alert("great!")
        }],
        error: [[function () {
            alert("second fuck");
        }]]
    })
}

AJS.toInit(function getImage() {
    baseUrl = AJS.params.baseUrl;
    pageID = AJS.params.pageId;
    AJS.$.ajax({
        url: baseUrl + '/rest/myrestresource/1.0/message/' + pageID,
        type: "GET",
        dataType: "json",
        success: [function (content) {
            $('#main').css({"background-image": "url(" + content.path + ')'})
        }],
        error: [function () {
            alert("second fuck");
        }]

    })
});

function generateUniqueFilname(input) {
    file = input.files[0];
    filename = input.files[0].name;
    Extension = filename.split('.').pop();
    AJS.$.ajax({
        url: baseUrl + '/rest/myrestresource/1.0/message',
        headers:{
          "filename": file,
        },
        type: "GET",
        dataType: "text",
        success: [function (uniqueFilename) {
            uniqueFilename += '.' + Extension;
            updateImage(file, uniqueFilename);
            alert("nice")
        }],
        error: [function () {
                alert("fuck:)")
        }]
    })
}

