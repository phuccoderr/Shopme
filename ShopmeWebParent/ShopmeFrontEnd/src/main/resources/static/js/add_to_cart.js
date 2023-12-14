$(document).ready(function () {
    $("#buttonAdd2Cart").on("click",function () {
        addToCart();
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