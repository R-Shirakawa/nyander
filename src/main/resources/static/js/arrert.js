function notifyOnCellChange() {
  // 監視する列の範囲を指定
  var sheet = SpreadsheetApp.getActiveSpreadsheet().getActiveSheet();
  var range = sheet.getRange("A:A"); // A列を監視する例

  // 監視を開始
  var values = range.getValues();
  var lastUpdatedValue = values[values.length - 1][0]; // 最後のセルの値を取得

  setInterval(function() {
    var currentValues = range.getValues();
    var currentUpdatedValue = currentValues[currentValues.length - 1][0]; // 現在の最後のセルの値を取得

    // 最後の更新値と現在の更新値を比較
    if (currentUpdatedValue !== lastUpdatedValue) {
      lastUpdatedValue = currentUpdatedValue; // 最後の更新値を更新
      sendNotification("セルが更新されました！");
    }
  }, 60000); // 1分ごとに監視
}

function sendNotification(message) {
  // 通知を送信するためのコードをここに追加
  // 例えば、メールを送信する場合はMailApp.sendEmail()を使用するなど
}