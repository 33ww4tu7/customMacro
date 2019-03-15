AJS.$(function () {
    AJS.$('.ffi input[type="file"]').fancyFileInput();
    AJS.$('#comment-save-button').click(function () {
        updateImage(" ");
        return false;
    });
});

function updateImage(input) {
    AJS.$.ajax({
        url: 'http://localhost:1990/confluence',
        type: "POST",
        success: [function () {
            if (input.files && input.files[0]) {
                var reader = new FileReader();
                reader.onload = function () {
                    $('#main').css({"background-image": "url("+ this.result +")"})
                };
                reader.readAsDataURL(input.files[0]);
                alert(input.files[0].toString());
            }
        }],
        error: [function () {
            alert("fuck")
        }]
    })
}
