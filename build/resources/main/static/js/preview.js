function preview(obj, previewId) {
    // Clear existing previews
    document.querySelector(`#${previewId}`).innerHTML = '';

    // 選択した全てのファイルに対してループ処理をします。
    for (i = 0; i < obj.files.length; i++) {
        // FileReaderオブジェクトを作成します。これによりブラウザ上でファイルを読み込むことが可能になります。
        let fileReader = new FileReader();

        // onloadイベントハンドラを設定します。ファイルが正常に読み込まれたときに呼び出されます。
        fileReader.onload = (e) => {
            if (obj.accept.includes('image')) {
                // 読み込んだ画像ファイルをData URLとしてimg要素に設定します。
                // これにより、選択した画像がブラウザ上でプレビュー表示されます。
                let imgElement = document.createElement('img');
                imgElement.src = e.target.result;

                // Add a class to the img element
                imgElement.classList.add('img-preview');

                // Append the img element to the specified preview div
                document.querySelector(`#${previewId}`).appendChild(imgElement);
            } else if (obj.accept.includes('video')) {
                // 読み込んだ動画ファイルをData URLとしてvideo要素に設定します。
                let videoElement = document.createElement('video');
                videoElement.src = e.target.result;
                videoElement.controls = true; // Add controls to the video element

                // Add a class to the video element
                videoElement.classList.add('video-preview');

                // Append the video element to the specified preview div
                document.querySelector(`#${previewId}`).appendChild(videoElement);
            }
        };

        // ファイルをData URLとして読み込みます。
        fileReader.readAsDataURL(obj.files[i]);
    }
}
