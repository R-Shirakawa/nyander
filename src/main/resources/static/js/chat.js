document.addEventListener("DOMContentLoaded", function() {
    var chatField = document.getElementById("chat-form");
    var chatError = true;
    chatField.addEventListener("input", function() {
            var chatValue = chatField.value;
            var feedbackElement = document.getElementById("error");

            if (chatValue.trim() === "") {
                chatField.classList.add("is-invalid");
                chatField.classList.remove("is-valid");
                feedbackElement.textContent ="文字を入力してください";
                chatError = true;
                if (!chatError) {
                            $('#submit-button').css('display', 'block');
                        } else {
                            $('#submit-button').css('display', 'none');
                        }
            } else if (chatValue.length > 140) {
                chatField.classList.add("is-invalid");
                chatField.classList.remove("is-valid");
                feedbackElement.textContent ="チャットは140文字以下で入力してください";
                chatError = true;
                if (!chatError) {
                            $('#submit-button').css('display', 'block');
                        } else {
                            $('#submit-button').css('display', 'none');
                        }
            } else {
            chatField.classList.add("is-valid");
                            chatField.classList.remove("is-invalid");
                            chatError = false;
            }
            if (!chatError) {
                $('#submit-button').css('display', 'block');
            } else {
                $('#submit-button').css('display', 'none');
            }
      })
      const chatArea = document.getElementById('test');
      chatArea.scrollTop = chatArea.scrollHeight - chatArea.clientHeight;
})
function chatGroupUpdatedCheck(callback) {
console.log("インターバル２")
    var chatGroupId = $("#chatGroupId").val();
    var chatId = $(".chat-unique-id").last().data("chat-id");
//    var chatId = $(".chat-unique-id").last().val();
console.log(chatGroupId)
        $.ajax({
            type: "GET",
            url: "/nyander/chatGroupUpdatedCheck/",
            data: {chatGroupId: chatGroupId, chatId: chatId},
            success: function(response) {
                callback(response);
            }
        })
    }
function updateCheck(){
    console.log("インターバル")
    chatGroupUpdatedCheck(function(isUpdated) {
        var chatGroupId = $("#chatGroupId").val();
        var petId = $("#pet-id").text();
        console.log(petId)
        if (isUpdated === "false") {
            // 更新処理
            $.ajax({
                type: "GET",
                url: "/nyander/re_chat_group/",
                data: {chatGroupId: chatGroupId, petId: petId},
                success: function(data) {
                $("#test").html(data);

                console.log("成功")
                },
            })
        }
    })
};
$(document).ready(function() {
    setInterval(updateCheck, 10000);
//    setInterval(updateCheck, 1000);
});
function signUpButtonDisplay() {
    if (!chatError) {
        $('#submit-button').css('display', 'block');
    } else {
        $('#submit-button').css('display', 'none');
    }
}

$(function() {
    const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
    const tooltipList = [...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl));
})