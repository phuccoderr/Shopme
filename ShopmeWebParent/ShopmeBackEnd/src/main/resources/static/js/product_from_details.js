$(document).ready(function () {
    $("a[name='linkRemoveDetail']").each(function (index) {
        $(this).click(function () {
            removeDetailByIndex(index);
        })
    })
})

function addDetails() {
    allDivDetails = $("[id^='divDetail']");
    divDetailsCount = allDivDetails.length;

    htmlDetailSection = `
        <div class="form-inline" id="divDetail${divDetailsCount}">
            <input type="hidden" name="detailIDs" value="0"/>
            <label class="m-3">Name:</label>
            <input type="text" class="form-control w-25" maxlength="255" name="detailNames"/>
            <label class="m-3">Value:</label>
            <input type="text" class="form-control w-25" maxlength="255" name="detailValues"/>
        </div>
    `;
    $("#divProductDetails").append(htmlDetailSection);

    previousDivDetailSection = allDivDetails.last();
    previosDivDetailId = previousDivDetailSection.attr("id");

    htmlLinkRemove = `
		<a class="btn fas fa-regular fa-circle-xmark icon-dark " 
			href="javascript:removeDetailByIndex('${previosDivDetailId}')"
			title="Remove this detail"> </a>
	`;

    previousDivDetailSection.append(htmlLinkRemove);

    $("input[name='detailNames']").last().focus();

}

function removeDetailByIndex(id) {
    $("#divDetail" + id).remove();
}







