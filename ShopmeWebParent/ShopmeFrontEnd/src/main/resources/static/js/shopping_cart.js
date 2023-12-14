decimalSeparator = decimalPointType == 'COMMA' ? ',' : '.';
thounsandsSeparator = thousandsPointType == 'COMMA' ? ',' : '.';
$(document).ready(function () {
    $(".link-Remove-Product").on("click",function (e) {
        e.preventDefault(); // ngan chan method get mac dinh cua trinh duyet
        removeProduct($(this));
    })
    
    $(".linkMinus").on("click",function (e) {
        e.preventDefault();
        decreaseQuantity($(this));
    })

    $(".linkPlus").on("click",function (e) {
        e.preventDefault();
        increaseQuantity($(this));
    })
    

})

function removeProduct(link) {
    url = link.attr("href");
    $.ajax({
        type: 'DELETE',
        url: url,
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function(response){
        alert(response);
        location.reload();
    }).fail(function(){
        alert("Lỗi xóa sản phẩm");
    });
}

function formatCurrency(amount) {
    return $.number(amount,decimalDigits,decimalSeparator,thounsandsSeparator);
}

function decreaseQuantity(link) {
    productId = link.attr("pid");
    quantityInput = $("#quantity" + productId);
    newQuantity = parseInt(quantityInput.val()) - 1;

    if (newQuantity > 0) {
        quantityInput.val(newQuantity);
        updateQuantity(productId,newQuantity);
    } else {
        alert("Không được nhỏ hơn 1!")
    }
}

function increaseQuantity(link) {
    productId = link.attr("pid");
    quantityInput = $("#quantity" + productId);
    newQuantity = parseInt(quantityInput.val()) + 1;

    if (newQuantity <= 5 ) {
        quantityInput.val(newQuantity);
        updateQuantity(productId, newQuantity);
    } else {
        alert("Sản phẩm kho hàng chỉ còn 5!");
    }
}

function updateQuantity(productId,quantity) {
    url = contextPath + "cart/update/" + productId + "/" + quantity;

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function(updatedSubtotal){
        updateSubtotal(updatedSubtotal,productId);
        updateTotal();
    }).fail(function(){
        alert("Them gio hang that bai");
    });
}

function updateSubtotal(updatedSubtotal,productId) {
    $("#subtotal" + productId).text(formatCurrency(updatedSubtotal));
}

function updateTotal() {
    total = 0.0;

    $(".subtotal").each(function(index,element) {
        total += parseFloat(clearCurrencyFormat(element.innerHTML));
    });


    $("#subtotals").text(formatCurrency(total));
    $("#total").text(formatCurrency(total += 20000));
}

function clearCurrencyFormat(numberString) {
    result = numberString.replaceAll(thounsandsSeparator,"");
    return result.replaceAll(decimalSeparator,"");
}

