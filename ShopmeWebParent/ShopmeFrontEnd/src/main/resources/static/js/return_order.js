var returnModal;
var modalTitle;
var fieldNote
var orderId;

$(document).ready(function () {
    returnModal = $("#returnOrderModal");
    modalTitle = $("#returnOrderModalTitle");
    fieldNote = $("#returnNote");

    $(".linkReturnOrder").on("click",function (e) {
        e.preventDefault();
        handleReturnOrderLink($(this));
    })
    
})

function handleReturnOrderLink(link) {
    orderId = link.attr("orderId");
    modalTitle.text("Return Order ID # " + orderId);
    returnModal.modal("show");

}

function submitReturnOrderForm() {
    reason = $("input[name='returnReason']:checked").val();
    note = fieldNote.val();

    sendReturnOrderRequest(reason,note);
    return false;
}

function sendReturnOrderRequest(reason,note) {
    requestURL = contextPath + "orders/return";
    requestBody = {orderId: orderId,reason: reason,note: note};

    $.ajax({
        type: 'POST',
        url: requestURL,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName,csrfValue);
        },
        data: JSON.stringify(requestBody),
        contentType: 'application/json'
    }).done(function (response) {
        alert("Sản phẩm đã được yêu cầu hoàn trả!")
        window.location.reload();
    }).fail(function () {
        alert("Server đang bị bảo trì!")
    })

}