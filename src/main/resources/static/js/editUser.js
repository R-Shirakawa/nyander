var imagePreview = document.getElementById("selectedImage");
var showImage = document.getElementById("selectedImage2");

window.addEventListener('DOMContentLoaded', function () {
    var image  = document.getElementById('selectedImage');
    var button = document.getElementById('crop-btn');
    var result = document.getElementById('imageUpload');
    // バリデーションで使用
    var nameField = document.getElementById("editName");
    var passwordField = document.getElementById("editPassword");
    var checkPasswordField = document.getElementById("editConfirmPassword");
    var postNumberField = document.getElementById("postNumber");
    var address1Field = document.getElementById("address1");
    var nameError = false;
    var passwordError = false;
    var checkPasswordError = false;
    var postNumberError = false;
    var address1Error = false;

    var croppable = false;
    var cropper = new Cropper(image, {
        aspectRatio: 1,
        viewMode: 1,
        ready: function () {
            croppable = true;
        },
    });

    document.getElementById('imageUpload').addEventListener('change', function () {

        const file = this.files[0];

        if (file) {
            const reader = new FileReader();
            //FileReaderを使用しデータのURL取得
            reader.onload = function (e) {
                cropper.replace(e.target.result);
            };
            reader.readAsDataURL(file);
        }
    });

    button.onclick = function () {
        var croppedCanvas;
        var roundedCanvas;
        var roundedImage;
        if (!croppable) {
            return;
        }

        croppedCanvas = cropper.getCroppedCanvas();
        roundedCanvas = getRoundedCanvas(croppedCanvas);
        // モーダルのimgタグを上書き
        roundedImage = document.createElement('img');
        roundedImage.src = roundedCanvas.toDataURL();
        result.innerHTML = '';
        result.appendChild(roundedImage);

        //ファイル化
        roundedCanvas.toBlob(function(imgBlob){
            // Blob を元に File化
            const croppedImgFile = new File([imgBlob], '切り抜き画像.png' , {type: "image/png"});
            const dt = new DataTransfer();
            dt.items.add(croppedImgFile);
            document.querySelector('input[name="image"]').files = dt.files;

            const file = dt.files[0];
            const fileSize = file.size;
            if(fileSize > 524288){
                alert('75MB以上のファイルはアップロードできません');
            } else {
                document.getElementById("selectedImage2").src = roundedCanvas.toDataURL();
                //モーダルを閉じる
                $("#close-modal-btn").trigger("click");
            }
        });
    };
    //ボタンの表示
    submitButtonDisplay()
    //バリデーション処理
    nameField.addEventListener("input", function() {
        var nameValue = nameField.value;
        var feedbackElement = nameField.nextElementSibling;
        if (nameValue.trim() === "") {
            nameField.classList.add("is-invalid");
            nameField.classList.remove("is-valid");
            feedbackElement.textContent = "名前を入力してください";
            nameError = true;
        } else if (nameValue.length > 10) {
            nameField.classList.add("is-invalid");
            nameField.classList.remove("is-valid");
            feedbackElement.textContent = "名前は10文字以下で入力してください";
            nameError = true;
        } else {
            // サーバーサイドに非同期リクエストを送信
            duplicationCheck(nameValue, function(isDuplicated) {
                if (isDuplicated === "true") {
                    nameField.classList.add("is-invalid");
                    nameField.classList.remove("is-valid");
                    feedbackElement.textContent = "入力されたアカウント名は既に登録されています";
                    nameError = true;
                    submitButtonDisplay();
                } else {
                    nameField.classList.remove("is-invalid");
                    nameField.classList.add("is-valid");
                    feedbackElement.textContent = "";
                    nameError = false;
                    submitButtonDisplay();
                }
            });
        }
        submitButtonDisplay();
    });

    function setPasswordResult(isError, message, feedbackElement) {
        if (isError) {
            passwordField.classList.add("is-invalid");
            passwordField.classList.remove("is-valid");
            feedbackElement.textContent = message;
            passwordError = true;
        } else {
            passwordField.classList.add('is-valid');
            passwordField.classList.remove("is-invalid");
            feedbackElement.textContent = "";
            passwordError = false;
        }
    }

    function setConfirmPasswordResult(isError, message, feedbackElement) {
        if (isError) {
            checkPasswordField.classList.add("is-invalid");
            checkPasswordField.classList.remove("is-valid");
            feedbackElement.textContent = message;
            checkPasswordError = true;
        } else {
            checkPasswordField.classList.add('is-valid');
            checkPasswordField.classList.remove("is-invalid");
            feedbackElement.textContent = "";
            checkPasswordError = false;
        }
    }

    //バリデーション処理(パスワード)
    passwordField.addEventListener("input", function() {
        var passwordValue = passwordField.value;
        var checkPasswordValue = checkPasswordField.value;
        var feedbackElement = passwordField.nextElementSibling;
        var feedbackElement2 = checkPasswordField.nextElementSibling;

        if (passwordValue === "") {             // パスワード未入力
            if (checkPasswordValue === "") {    // 確認用パスワード未入力
                setPasswordResult(false, "", feedbackElement);
                setConfirmPasswordResult(false, "", feedbackElement2);
            } else {
                setPasswordResult(false, "", feedbackElement);
                setConfirmPasswordResult(true, "確認用パスワードが一致しません", feedbackElement2);
            }
        } else if (passwordValue.trim() === "") {   // パスワードに空白文字のみ
            setPasswordResult(true, "パスワードを入力してください", feedbackElement);
        } else if (passwordValue.length < 6　|| passwordValue.length > 21) {
            setPasswordResult(true, "パスワードは6文字以上20文字以下で入力してください", feedbackElement);
        } else {
            setPasswordResult(false, "", feedbackElement);
            checkPasswordError = true;
        }
        submitButtonDisplay();
    });

    //バリデーション処理(パスワード2)
    checkPasswordField.addEventListener("input", function() {
        var passwordValue = passwordField.value;
        var checkPasswordValue = checkPasswordField.value;
        var feedbackElement = passwordField.nextElementSibling;
        var feedbackElement2 = checkPasswordField.nextElementSibling;

        if (checkPasswordValue === "") {    // 確認用パスワード未入力
            if (passwordValue === "") {     // パスワード未入力
                setPasswordResult(false, "", feedbackElement);
                setConfirmPasswordResult(false, "", feedbackElement2);
            } else {
                setConfirmPasswordResult(true, "確認用パスワードが一致しません", feedbackElement2);
            }
        } else if (checkPasswordValue.trim() === "") {
            setConfirmPasswordResult(true, "確認用パスワードを入力してください", feedbackElement2);
        } else if (checkPasswordValue !== passwordValue) {
            setConfirmPasswordResult(true, "確認用パスワードが一致しません", feedbackElement2);
        } else {
            setConfirmPasswordResult(false, "", feedbackElement2);
        }
        submitButtonDisplay();
    });

    passwordField.addEventListener("input", function() {
        var passwordValue = passwordField.value;
        var checkPasswordValue = checkPasswordField.value;
        var feedbackElement = passwordField.nextElementSibling;
        var feedbackElement2 = checkPasswordField.nextElementSibling;

        if(checkPasswordValue.trim() === "") {
            setConfirmPasswordResult(true, "確認用パスワードを入力してください", feedbackElement2);
        } else if (checkPasswordValue !== passwordValue) {
            setConfirmPasswordResult(true, "確認用パスワードが一致しません", feedbackElement2);
        } else {
            setConfirmPasswordResult(false, "", feedbackElement2);
        }
        submitButtonDisplay();
    });

    postNumberField.addEventListener("input", function() {
        var postNumberValue = postNumberField.value;
        var feedbackElement = postNumberField.nextElementSibling;
        var regex = /^[0-9]*$/;

        if (!postNumberValue.trim()){
            postNumberField.classList.remove("is-invalid");
            postNumberField.classList.add("is-valid");
            feedbackElement.textContent = "";
            postNumberError = false;
        } else if (!regex.test(postNumberValue) || postNumberValue.length !== 7) {
            postNumberField.classList.add("is-invalid");
            postNumberField.classList.remove("is-valid");
            feedbackElement.textContent = "郵便番号には7桁(ハイフンなし)の数字を入力してください";
            postNumberError = true;
        }
         else {
            postNumberField.classList.remove("is-invalid");
            postNumberField.classList.add("is-valid");
            feedbackElement.textContent = "";
            postNumberError = false;
        }
        signUpButtonDisplay();
    });

    address1Field.addEventListener("input", function() {
        var feedbackElement = document.getElementById("feedbackAddress1");

        if ($('#address1 option:selected').val() === "") {
            address1Field.classList.add("is-invalid");
            address1Field.classList.remove("is-valid");
            feedbackElement.textContent = "都道府県が選択されていません";
            address1Error = true;
        } else {
            feedbackElement.textContent = "";
            address1Field.classList.remove("is-invalid");
            address1Field.classList.add("is-valid");
            address1Error = false;
        }
        submitButtonDisplay();
    });

    function submitButtonDisplay() {
        if (!nameError && !passwordError && !checkPasswordError && !postNumberError && !address1Error) {
            $('.submit-button').css('display', 'block');
        } else {
            $('.submit-button').css('display', 'none');
        }
    }

    function duplicationCheck(name, callback) {
        $.ajax({
            type: "GET",
            url: "/nyander/duplicationCheck/",
            data: {name: name},
            success: function(response) {
                callback(response);
            }
        })
    }
});

// 画像を丸型にする
function getRoundedCanvas(sourceCanvas) {
    var canvas = document.createElement('canvas');
    var context = canvas.getContext('2d');
    var width = sourceCanvas.width;
    var height = sourceCanvas.height;

    canvas.width = width;
    canvas.height = height;
    context.imageSmoothingEnabled = true;
    context.drawImage(sourceCanvas, 0, 0, width, height);
    context.globalCompositeOperation = 'destination-in';
    context.beginPath();
    context.arc(width / 2, height / 2, Math.min(width, height) / 2, 0, 2 * Math.PI, true);
    context.fill();
    return canvas;
}

// 郵便番号検索
$(document).ready(function(){

    // 住所検索ボタンを押すと外部apiを叩く処理が走る。
    $('#searchAddress').click(function() {
        $.getJSON('http://zipcloud.ibsnet.co.jp/api/search?callback=?',
            {
            zipcode: $('#postNumber').val()
            }
        )
        .done(function(data) {
            if (!data.results) {
                alert('該当の住所がありません');
            } else {
                let result = data.results[0];

                // セレクトボックスのoption要素を取得
                const options = $("select option");
                // option要素のinnerTextがaddressと一致するものを選択状態にする
                options.each(function() {
                  if ($(this).text() === result.address1) {
                    $(this).prop("selected", true);
                  }
                });
                $('#address2').val(result.address2);
                $('#address3').val(result.address3);
            }
        }).fail(function(){
            alert('入力値を確認してください。');
        })
    })

    // クリアボタンを押すと、フォームの中身がリセットされる。
    $('#clearAddress').click(function(){
        $('#postNumber').val('');
        $('#address1').val('');
        $('#address2').val('');
        $('#address3').val('');
    })
})