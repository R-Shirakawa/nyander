window.onload = function() {
    // 画面ロード時に値が入っていれば、ボタンを有効化
    if ($("#email").val() !== "" && $("#password").val() !== "") {
//        $('#loginButton').prop("disabled", false);
        $('.submit-button').css('display', 'block');
    }
};

document.addEventListener("DOMContentLoaded", function() {
    var nameField = $("#email");    // Spring Security正常動作のためにemailと記述するが、中身はname
    var passwordField = $("#password");

    // 値の入力チェック
    $(document).on("input", function() {
        if (nameField.val() !== "" && passwordField.val() !== "") {
//            $('#loginButton').prop("disabled", false);
            $('.submit-button').css('display', 'block');
        } else {
//            $('#loginButton').prop("disabled", true);
            $('.submit-button').css('display', 'none');
        }
    });
});