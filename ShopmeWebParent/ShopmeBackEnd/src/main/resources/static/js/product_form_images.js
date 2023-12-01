var extraImagesCount = 0;
$(document).ready(function () {

    $("input[name='extraImage']").each(function (index) {
        extraImagesCount++;
        $(this).change(function () {
            showExtraImageThumbnail(this,index);
        })
    })
    $("a[name='linkRemoveExtraImage']").each(function (index) {
        $(this).click(function () {
            removeExtraImage(index);
        })
    })
})

function showExtraImageThumbnail(fileInput,index) {
    var file = fileInput.files[0];
    var reader = new FileReader();

    fileName = file.name;
    imageNameHiddenField = $("#imageName" + index);
    if (imageNameHiddenField.length) {
        imageNameHiddenField.val(fileName);
    }

    reader.onload = function (e) {
        $("#extraThumbnail" + index).attr("src", e.target.result);
    };
    reader.readAsDataURL(file);
    //tien hanh tao them lua chon anh extra

    if (index >= extraImagesCount - 1) {
        addNextExtraImageSection(index + 1);
    }

}

function addNextExtraImageSection(index) {
    html = `<div class="col-4 border m-3 p-2" id="divExtraImage${index}">
                <div id="extraImageHeader${index}">
                    <label>Extra Image:</label>                
                </div>
                <div>
                    <img id="extraThumbnail${index}"  class="h-75 w-75"
                         src="${defaultImageThumbnailSrc}">
                </div>
                <div class="m-2">
                 <input type="file"  name="extraImage"
                       onchange="showExtraImageThumbnail(this, ${index})"
                       accept="image/png, image/jpeg"/>
                 </div>
            </div>
    `;

    htmlLinkRemove = `
		<a class="btn fas fa-regular fa-circle-xmark icon-dark float-right" 
			href="javascript:removeExtraImage(${index - 1})"
			title="Remove this image"> </a>
	
	`;
    $("#divProductImages").append(html); //chung se onchange thang vao function showExtraImageThumbnail
    // de chay duoc qua inner html
    $("#extraImageHeader" + (index - 1)).append(htmlLinkRemove);
    extraImagesCount++;
}

function removeExtraImage(index) {
    $("#divExtraImage" + index).remove();
}