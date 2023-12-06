$(document).ready(function() {
    $(".collapsed").on("click",function (e) {
        e.preventDefault();
        $("#collapseTable").toggleClass("show");
    });
    showNavBar();
    logoutAccount();
    showProfileAdmin();
    showAccountDetail();
    showSetting();
});

function  showNavBar() {
    $("#sidebarToggle").on("click",function(e) {
        e.preventDefault();
        var isCollapsed = $("#accordionSidebar").hasClass("toggled");
        if (isCollapsed) {
            $("#accordionSidebar").removeClass("toggled");
        } else {
            $("#accordionSidebar").addClass("toggled");
        }
    })
}
function logoutAccount() {
    $("#logoutLink").on("click",function (e) {
        e.preventDefault();
        document.logoutForm.submit();
    })

}

function showProfileAdmin() {
    $(".dropdown").on("click",function (e) {
        e.preventDefault();
        $(".dropdown-menu-right").toggleClass("show");
    })

}

function showAccountDetail () {
    $(".link-Account").on("click",function (e) {
        e.preventDefault();
        linkDetailURL = $(this).attr("href");
        $("#detailModal").modal("show").find(".modal-content").load(linkDetailURL);
    })
}

function showSetting() {
    $(".link-Setting").on("click",function (e) {
        e.preventDefault();
        linkDetailURL = $(this).attr("href");
        window.location.href = linkDetailURL;
    })
}
