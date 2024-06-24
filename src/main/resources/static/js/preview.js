function preview(obj, previewId) {
    
    document.querySelector(`#${previewId}`).innerHTML = '';

    for (i = 0; i < obj.files.length; i++) {
        let fileReader = new FileReader();

        // ファイルが正常に読み込まれたときに呼び出す
        fileReader.onload = (e) => {
            if (obj.accept.includes('image')) {
                // 読み込んだ画像ファイルをData URLとしてimg要素に設定
                let imgElement = document.createElement('img');
                imgElement.src = e.target.result;

                imgElement.classList.add('img-preview');

                document.querySelector(`#${previewId}`).appendChild(imgElement);
            } else if (obj.accept.includes('video')) {
                // 読み込んだ動画ファイルをData URLとしてvideo要素に設定
                let videoElement = document.createElement('video');
                videoElement.src = e.target.result;
                videoElement.controls = true;
                videoElement.classList.add('video-preview');
                document.querySelector(`#${previewId}`).appendChild(videoElement);
            }
        };

        // ファイルをData URLとして読み込み
        fileReader.readAsDataURL(obj.files[i]);
    }
}
