document.addEventListener("DOMContentLoaded", function() {
    var nameField = document.getElementById("signUpName");
    var passwordField = document.getElementById("signUpPassword");
    var checkPasswordField = document.getElementById("confirmPassword");
    var postNumberField = document.getElementById("postNumber");
    var address1Field = document.getElementById("address1");
    var nameError = true;
    var passwordError = true;
    var checkPasswordError = true;
    var postNumberError = false; // nullを許容するため、初期値はエラーではない状態とする
    var address1Error = true;

    //名前のエラー処理
    nameField.addEventListener("input", function() {
        var nameValue = nameField.value;
        var feedbackElement = nameField.nextElementSibling;
        var regex = /^[0-9a-zA-Z]*$/;

        if (nameValue.trim() === "") {
            nameField.classList.add("is-invalid");
            nameField.classList.remove("is-valid");
            feedbackElement.textContent ="名前を入力してください";
            nameError = true;
        } else if (nameValue.length > 10 || !regex.test(nameValue)) {
            nameField.classList.add("is-invalid");
            nameField.classList.remove("is-valid");
            feedbackElement.textContent ="名前は半角英数字10文字以下で入力してください";
            nameError = true;
        } else {
            // サーバーサイドに非同期リクエストを送信
            duplicationCheck(nameValue, function(isDuplicated) {
                if (isDuplicated === "true") {
                    nameField.classList.add("is-invalid");
                    nameField.classList.remove("is-valid");
                    feedbackElement.textContent = "入力されたアカウント名は既に登録されています";
                    nameError = true;
                } else {
                    nameField.classList.remove("is-invalid");
                    nameField.classList.add("is-valid");
                    feedbackElement.textContent = "";
                    nameError = false;
                }
            });
        }
        signUpButtonDisplay();
    })

    //パスワードのエラー処理
    passwordField.addEventListener("input", function() {
        var passwordValue = passwordField.value;
        var feedbackElement = passwordField.nextElementSibling;

        if(passwordValue.trim() === "") {
            passwordField.classList.add("is-invalid");
            passwordField.classList.remove('is-valid');
            feedbackElement.textContent = "パスワードを入力してください";
            passwordError = true;
        } else if(passwordValue.length < 6　|| passwordValue.length > 20) {
            passwordField.classList.add("is-invalid");
            passwordField.classList.remove('is-valid');
            feedbackElement.textContent = "パスワードは6文字以上20文字以下で入力してください";
            passwordError = true;
        } else {
            feedbackElement.textContent = ""; // エラーメッセージをクリア
            passwordField.classList.add('is-valid');
            passwordField.classList.remove("is-invalid");
            passwordError = false;
        }
        signUpButtonDisplay();
    })

    //確認用パスワードのエラー処理
    checkPasswordField.addEventListener("input", function() {
        var checkPasswordValue = checkPasswordField.value;
        var passwordValue = passwordField.value;
        var feedbackElement = checkPasswordField.nextElementSibling;

        if(checkPasswordValue.trim() === "") {
            checkPasswordField.classList.add("is-invalid");
            checkPasswordField.classList.remove("is-valid");
            feedbackElement.textContent = "確認用パスワードを入力してください";
            checkPasswordError = true;
        } else if (checkPasswordValue !== passwordValue) {
            checkPasswordField.classList.add("is-invalid");
            checkPasswordField.classList.remove("is-valid");
            feedbackElement.textContent = "確認用パスワードが一致しません";
            checkPasswordError = true;
        } else {
            checkPasswordField.classList.remove("is-invalid");
            checkPasswordField.classList.add("is-valid");
            feedbackElement.textContent = "";
            checkPasswordError = false;
        }
        signUpButtonDisplay();
    });

    passwordField.addEventListener("input", function() {
        var checkPasswordValue = checkPasswordField.value;
        var passwordValue = passwordField.value;
        var feedbackElement = checkPasswordField.nextElementSibling;

        if(checkPasswordValue.trim() === "") {
            checkPasswordField.classList.add("is-invalid");
            checkPasswordField.classList.remove("is-valid");
            feedbackElement.textContent = "確認用パスワードを入力してください";
            checkPasswordError = true;
        } else if (checkPasswordValue !== passwordValue) {
            checkPasswordField.classList.add("is-invalid");
            checkPasswordField.classList.remove("is-valid");
            feedbackElement.textContent = "確認用パスワードが一致しません";
            checkPasswordError = true;
        } else {
            checkPasswordField.classList.remove("is-invalid");
            checkPasswordField.classList.add("is-valid");
            feedbackElement.textContent = "";
            checkPasswordError = false;
        }
        signUpButtonDisplay();
    });

    postNumberField.addEventListener("input", postNumberValidation);

    function postNumberValidation() {
        var postNumberValue = postNumberField.value;
        var feedbackElement = postNumberField.nextElementSibling;
        var regex = /^[0-9]*$/;

        if (!postNumberValue.trim()){
        postNumberField.classList.remove("is-invalid");
                    postNumberField.classList.add("is-valid");
                    feedbackElement.textContent = "";
                    postNumberError = false;
        }
       else if (!regex.test(postNumberValue) || postNumberValue.length !== 7) {
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
    }

    address1Field.addEventListener("input", address1Validation);

    function address1Validation() {
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
        signUpButtonDisplay();
    }

    function signUpButtonDisplay() {
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
                   postNumberValidation();
                   address1Validation();
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
});