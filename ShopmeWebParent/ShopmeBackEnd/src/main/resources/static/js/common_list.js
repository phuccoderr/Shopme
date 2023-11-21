$(document).ready(function () {
    $(".link-detail").on("click",function (e) {
        e.preventDefault();
        linkDetailURL = $(this).attr("href");
        $("#detailModal").modal("show").find(".modal-content").load(linkDetailURL);
    })

    $("#linkAddNew").on("click",function (e) {
        e.preventDefault();
        linkDetailURL = $(this).attr("href");
        $("#detailModal").modal("show").find(".modal-content").load(linkDetailURL);
    })


})

function clearFilter() {
    window.location = moduleURL;
}



function showDeleteConfirmModal(link,entityName) {
    entityId = link.attr("entityId");

    $("#yesButton").attr("href",link.attr("href"));
    $("#confirmText").text("Are you sure you want to delete this " + entityName + " ID " + entityId + "?");
    $("#confirmModal").modal("show");
}