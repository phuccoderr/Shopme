var buttonLoadCountries;
var dropDownCountry ;
var buttonAddCountry;
var buttonUpdateCountry;
var buttonDeleteCountry;
var labelCountryName;
var fieldCountryName;
var fieldCountryCode;
$(document).ready(function () {
    buttonLoadCountries = $("#buttonLoadCountries");
    dropDownCountry = $("#dropDownCountries");
    buttonAddCountry = $("#buttonAddCountry");
    buttonUpdateCountry = $("#buttonUpdateCountry");
    buttonDeleteCountry = $("#buttonDeleteCountry");
    labelCountryName = $("#labelCountryName");
    fieldCountryName = $("#fieldCountryName");
    fieldCountryCode = $("#fieldCountryCode");

    buttonLoadCountries.on("click",function (e) {
        e.preventDefault();
        loadCountries();
        if(buttonLoadCountries.val() == "Refresh Country List") {
            $(this).val("Load Countries");
        }
    })

    dropDownCountry.on("change",function (e) {
        e.preventDefault();
        changeFormStateToSelectedCountry();
    })

    buttonAddCountry.on("click",function (e) {
        e.preventDefault();
        if($(this).val() == "Add") {
            addCountry();
        } else {
            changeFormStateToNewCountry();
        }
    })

    buttonUpdateCountry.on("click",function (e) {
        e.preventDefault();
        updateCountry();
    })

    buttonDeleteCountry.on("click",function (e) {
        e.preventDefault();
        deleteCountry();
    })
})
function loadCountries() {
    url = contextPath + "countries/list";
    $.get(url,function (countryJSON) {
        dropDownCountry.empty();
        $.each(countryJSON,function (index,country) {
            $("<option>").val(country.id + "-" + country.code).text(country.name).appendTo(dropDownCountry);
        })
    })
}

function changeFormStateToSelectedCountry() {
    buttonAddCountry.prop("value","Change");
    buttonUpdateCountry.prop("disabled",false);
    buttonDeleteCountry.prop("disabled",false);

    labelCountryName.text("Selected Country:");

    selectCountry = $("#dropDownCountries option:selected").text();

    fieldCountryName.val(selectCountry);

    countrycode = dropDownCountry.val().split("-")[1];
    fieldCountryCode.val(countrycode);

}

function addCountry() {
    countryName = fieldCountryName.val();
    countryCode = fieldCountryCode.val();
    url = contextPath + "countries/save";

    JsonDATA = {name: countryName,code: countryCode};
    $.ajax({
        type:'POST',
        url: url,
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeaderName,csrfValue);
        },
        data: JSON.stringify(JsonDATA),
        contentType: 'application/json'
    }).done(function (countryId) {
        selectedNewAddedCountry(countryId,countryCode,countryName);
    }).fail(function () {
        alert("error");
    })
}

function selectedNewAddedCountry(countryId,countryCode,countryName) {
    optionValue = countryCode;
    $("<option>").val(optionValue).text(countryName).appendTo(dropDownCountry);

    $("#dropDownCountries option[value='" + optionValue + "']").prop("selected",true);

    fieldCountryCode.val("");
    fieldCountryName.val("").focus();
}

function changeFormStateToNewCountry() {
    buttonAddCountry.val("Add");
    labelCountryName.text("Country name:");

    buttonUpdateCountry.prop("disabled",true);
    buttonDeleteCountry.prop("disabled",true);

    fieldCountryCode.val("");
    fieldCountryName.val("").focus();
}

function updateCountry() {
    countryName = fieldCountryName.val();
    countryCode = fieldCountryCode.val();
    url = contextPath + "countries/save";

    countryId = dropDownCountry.val().split("-")[0];

    JsonDATA = {id: countryId, name: countryName,code: countryCode};
    $.ajax({
        type:'POST',
        url: url,
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeaderName,csrfValue);
        },
        data: JSON.stringify(JsonDATA),
        contentType: 'application/json'
    }).done(function (countryId) {
        $("#dropDownCountries option:selected").val(countryId + "-" + countryCode);
        $("#dropDownCountries option:selected").text(countryName);
    }).fail(function () {
        alert("error");
    })
}

function deleteCountry() {
    optionValue = dropDownCountry.val();
    countryId = dropDownCountry.val().split("-")[0];
    url = contextPath + "countries/delete/" + countryId;

    $.get(url,function () {
        $("#dropDownCountries option[value='" + optionValue + "']").remove();
        changeFormStateToNewCountry();
    }).done(function() {
        buttonLoadCountries.val("Refresh Country List");
        alert("All countries have been loaded");
    }).fail(function() {
        alert("ERROR: Could not connect to server or server encountered an error");
    });
}