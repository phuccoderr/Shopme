dropdownMenu = $(".dropdown-top-menu");
$(document).ready(function () {
    $(".dropdown-hover").each(function () {
        $(this).hover(function () {
            $(this).children('ul').addClass("d-flex").show()
        },function () {
            $(this).children('ul').removeClass("d-flex").hide()
        })


    })

})

