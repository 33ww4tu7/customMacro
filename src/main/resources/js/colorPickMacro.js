AJS.$(function () {
    AJS.$('.ffi input[type="file"]').fancyFileInput();
    AJS.$('#comment-save-button').click(function () {
        updateImage(" ");
        return false;
    });
});

function updateImage(input, userID, pageID) {
    file = input.files[0];
    fileName = input.files[0].name;
    console.log(file);
    var actionData = new FormData();
    actionData.append('file', file);
    actionData.append('comment', "foobar");
    actionData.append('minorEdit', "true");
    AJS.$.ajax({
        url: 'http://localhost:1990/confluence/rest/api/content/' + pageID + '/child/attachment',
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
            // var res = JSON.parse(dataType.Response);
            alldata = content.results;
            lastImage = alldata[alldata.length - 1];
            link = lastImage._links.download;
            fullpath = "http://localhost:1990/confluence" + link;
            $('#main').css({"background-image": "url("+fullpath+')'});
            alert(link);
            uploadDB(fullpath, pageID, userID)
        }],
        error: [function () {
            alert("fuck")
        }]
    })
}

function uploadDB(fullpath, pageID, userID) {
    AJS.$.ajax({
        url: 'http://localhost:1990/confluence/rest/myrestresource/1.0/message/set/' + pageID + '/' + userID,
        type: "GET",
        dataType: "xml",
        headers: {
            "path": fullpath
        },
        success: [function () {
            alert("great!")
        }],
        error: [[function () {
            alert("second fuck");
        }]]
    })
}

/*function getPath(pageID, userID) {
    AJS.$.ajax({
        url: 'http://localhost:1990/confluence/rest/myrestresource/1.0/message/' + pageID + '/' + userID,
        type: "GET",
        dataType: "xml",
        success: [function (){
                xmlDoc = $.parseXML("xml"),
                $xml = $(xmlDoc),
                $path = $xml.find("url");
                alert($path);
        }],
        error: [function () {
            alert("fuck")
        }]
    })
}*/

