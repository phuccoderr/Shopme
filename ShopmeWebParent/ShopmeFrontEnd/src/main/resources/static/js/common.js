dropdownMenu = $(".dropdown-top-menu");
$(document).ready(function () {
    $(".dropdown-hover").each(function () {
        $(this).hover(function () {
            $(this).children('ul').addClass("d-flex").show()
        },function () {
            $(this).children('ul').removeClass("d-flex").hide()
        })
    })

    $(".dropdownClick").on("click",function() {

        if ($(this).children('ul').hasClass('d-flex')) {
            $(this).children('ul').removeClass('d-flex')
        } else {
            $(this).children('ul').addClass("d-flex")
        }
    })

    logoutAccount();
})

function logoutAccount() {
    $("#logoutLink").on("click",function (e) {
        e.preventDefault();
        document.logoutForm.submit();
    })

}
