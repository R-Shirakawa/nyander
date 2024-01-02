$(function(){
  $(".submit-select").change(function(){
    $(this).closest(".search-form").submit();
  });
});

    // 画面スクロールの制御
    const onScroll = ()=> {
        // ターゲットのtop位置を取得
        position = document.querySelector('.page-title').getBoundingClientRect().top;

        // ウィンドウ高さより小さい場合に1回だけ実行
        if (position <= window.innerHeight) {
            window.removeEventListener('scroll', onScroll, false);
            const targetDOMRect = document.querySelector('.page-title').getBoundingClientRect();
            const targetTop = targetDOMRect.top + window.pageYOffset - 80;

            event.preventDefault();
            $('html, body').stop().animate({scrollTop: targetTop}, 100);
        }
    }

    function setScroll() {
        window.addEventListener('scroll',onScroll, false);
    }

    setTimeout(setScroll, 3000);