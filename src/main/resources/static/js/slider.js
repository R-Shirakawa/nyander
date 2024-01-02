$(function(){
    $('.carousel').slick({
        slidesToShow: 3, // スライドに表示する画像の枚数
        slidesToScroll: 1, // スライド時に何枚分切り替えるかの枚数
        autoplay: true, // 自動でスライドを行う
        autoplaySpeed: 3000, // 自動でスライドが切り替わるミリ秒数
    });
});