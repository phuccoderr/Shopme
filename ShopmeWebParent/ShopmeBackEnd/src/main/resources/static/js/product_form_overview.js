dropdownsBrands = $("#brand");
dropdownsCategories = $("#category");
$(document).ready(function () {
    dropdownsBrands.change(function() {

        dropdownsCategories.empty();
        getCategories($(this).val());

    })
    $("#shortDescription").richText();
    $("#fullDescription").richText();



})
function getCategories(brandId) {
    url = brandModuleURL + "/" + brandId + "/categories";
    $.get(url,function (responseJSON) {
        $.each(responseJSON,function (index,category) {
            $("<option>").val(category.id).text(category.name).appendTo(dropdownsCategories);
        });
    });
}

function checkUnique(form) {
    productId = $("#id").val();
    productName = $("#name").val();
    _csrfValue = $("input[name='_csrf']").val();

    url = contextPath + "products/checkUnique";
    params = {id: productId, name: productName, _csrf: _csrfValue};


    $.post(url,params,function (response) {
        if (response == "OK") {
            form.submit();
        } else {
            alert("da co product voi name la: " + productName);
        }
    }).fail (function () {
        alert("Cloud not connect to the server");
    })
    return false;
}