$(document).ready(function () {
    $(".linkVoteReview").on("click",function (e) {
        e.preventDefault();
        voteReviews($(this));
    })
})

function voteReviews(currentLink) {
    requestURL = currentLink.attr("href");


    $.ajax({
        type: 'POST',
        url: requestURL,
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function(voteResult){
        if (voteResult.successful) {
            updateVoteCountAndIcons(currentLink,voteResult);
        }
        if (!voteResult.successful) {
            alert(voteResult.message);
        }
    }).fail(function(){
        alert("vote đã bị lỗi!");
    });
}

function updateVoteCountAndIcons(curentLink,voteResult) {
    reviewId = curentLink.attr("reviewId");
    voteUpLink = $("#linkVoteUp-" + reviewId);
    voteDownLink = $("#linkVoteDown-" + reviewId);
    voteCount = $("#voteCount-" + reviewId);

    voteCount.text(voteResult.voteCount + " Votes");

    message = voteResult.message;
    if(message.includes("successfully voted up")) {
        highlightVoteUpIcon(curentLink,voteDownLink);
    } else if(message.includes("successfully voted down")) {
        highlightVoteDownIcon(curentLink,voteUpLink);
    } else if(message.includes("unvoted")) {
        unHighlightVoteDownIcon(voteDownLink,voteUpLink);
    }
}
function highlightVoteUpIcon(voteUpLink,voteDownLink) {
    voteUpLink.removeClass("fa-regular").addClass("fa-solid");
    voteDownLink.removeClass("fa-solid").addClass("fa-regular");
}
function highlightVoteDownIcon(voteDownLink,voteUpLink) {
    voteDownLink.removeClass("fa-regular").addClass("fa-solid");
    voteUpLink.removeClass("fa-solid").addClass("fa-regular");

}

function unHighlightVoteDownIcon(voteDownLink,voteUpLink) {
    if (voteUpLink.hasClass("fa-solid")) {
        voteUpLink.removeClass("fa-solid").addClass("fa-regular");
    }
    if (voteDownLink.hasClass("fa-solid")) {
        voteDownLink.removeClass("fa-solid").addClass("fa-regular");
    }
}
