$(document).ready(function () {
    $("#buttonAdd2Cart").on("click",function () {
        addToCart();
    })
    $("#buttonBuyAndAdd2Cart").on("click",function () {
        addToCartAndRedirect();
    })
})
function addToCart() {
    url = contextPath + "cart/add/" + productId + "/" + 1;

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function(response){
        alert(response);
    }).fail(function(){
        alert("Them gio hang that bai");
    });
}

function addToCartAndRedirect() {
    url = contextPath + "cart/add/" + productId + "/" + 1;

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function(response){
        alert(response);
        if (response.includes("sản phẩm này đã được thêm vào giỏ hàng của bạn.")) {
            window.location.href = contextPath + "cart";
        }
    }).fail(function(){
        alert("Them gio hang that bai");
    });
}