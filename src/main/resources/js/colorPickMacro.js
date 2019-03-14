AJS.$(function () {
    AJS.$('.ffi input[type="file"]').fancyFileInput();
});

function updateImage(input, pageId) {
    var file = input.files[0];
    console.log(file);
    var formdata = new FormData();
    formdata.append("file", file);
    formdata.append("comment", "background-page");
    formdata.append("minorEdit", "true");
    AJS.$.ajax({
        url: "http://localhost:1990/confluence/rest/api/content/" + pageId + "/child/attachment",
        type: "POST",
        contentType: false,
        data: formdata,
        processData: false,
        headers: {
            "Authorization": "Basic " + btoa("admin:admin"),
            "X-Atlassian-Token": "nocheck"
        },

        success: [function () {
            if (input.files && input.files[0]) {
                var reader = new FileReader();
                reader.onload = function () {
                    $('#main').css({"background-image": "url(" + this.result + ")"})
                };
                reader.readAsDataURL(input.files[0]);
                alert(input.files[0].toString());
            }
        }],
        error: [function () {
            alert("fuck")
        }]
    })
    //}
}


