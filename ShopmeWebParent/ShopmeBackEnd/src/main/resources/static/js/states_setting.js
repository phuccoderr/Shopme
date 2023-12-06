var buttonLoad4States;
var dropDownCountry4States;
var labelStateName;
var buttonAddState;
var buttonUpdateState;
var buttonDeleteState;
var fieldStateName;
var dropDownStates;

$(document).ready(function () {
    buttonLoad4States = $("#buttonLoadForStates");
    dropDownCountry4States = $("#dropDownCountryForStates");
    dropDownStates = $("#dropDownStates");
    labelStateName = $("#labelStateName");
    buttonAddState = $("#buttonAddState");
    buttonUpdateState = $("#buttonUpdateState");
    buttonDeleteState = $("#buttonDeleteState");
    fieldStateName = $("#fieldStateName");

    buttonLoad4States.on("click",function (e) {
        e.preventDefault();
        loadCountries4States();
    })

    dropDownCountry4States.on("change",function (e) {
        e.preventDefault();
        loadStates4Country();
    })

    dropDownStates.on("change",function (e) {
        e.preventDefault();
        changeForm4StateToSelectedCountry()
    })

    buttonAddState.on("click",function (e) {
        e.preventDefault();
        addStateByCountry();
    })

    buttonUpdateState.on("click",function (e) {
        e.preventDefault();
        updateStateByCountry();
    })

    buttonDeleteState.on("click",function (e) {
        e.preventDefault();
        deleteStateByCountry();
    })
})

function loadCountries4States() {
    url = contextPath + "countries/list";
    $.get(url,function (countryJSON) {
        dropDownCountry.empty();
        $.each(countryJSON,function (index,country) {
            $("<option>").val(country.id).text(country.name).appendTo(dropDownCountry4States);
        })
    })
}

function changeForm4StateToSelectedCountry() {
    buttonAddState.prop("value","change");
    buttonUpdateState.prop("disabled",false);
    buttonDeleteState.prop("disabled",false);

    labelStateName.text("Selected State/Province");

    selectedStateName = $("#dropDownStates option:selected").text();
    fieldStateName.val(selectedStateName);
}

function loadStates4Country() {
    selectCountry = $("#dropDownCountryForStates option:selected").val();
    url = contextPath + "states/list_by_country/" + selectCountry;
    $.get(url,function (listResponse) {
        dropDownStates.empty();
        $.each(listResponse,function (index,state) {
            $("<option>").val(state.id).text(state.name).appendTo(dropDownStates);
        })
    }).done(function () {
        
    }).fail(function () {
        alert("Error")
    })
}

function addStateByCountry() {
    url = contextPath + "states/save";

    stateName = fieldStateName.val();

    selectCountry = $("#dropDownCountryForStates option:selected");
    countryId = selectCountry.val();
    countryName = selectCountry.text();

    jsonDataState = { name: stateName, country: {id :countryId, name: countryName}};

    $.ajax({
        url: url,
        type: 'POST',
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName,csrfValue);
        },
        data: JSON.stringify(jsonDataState),
        contentType: 'application/json'

    }).done(function (stateId) {
        selectNewAddState(stateId,stateName);
        alert(stateId + " has been add");
    }).fail(function () {
        alert("fail");
    })
}

function selectNewAddState(stateId,stateName) {
    optionValue = stateId;
    $("<option>").val(optionValue).text(stateName).appendTo(dropDownStates);

    $("#dropDownStates option[value='" + optionValue + "']").prop("selected",true);
    fieldStateName.val("").focus();
}

function updateStateByCountry() {
    url = contextPath + "states/save";
    stateName = fieldStateName.val();
    stateId = $("#dropDownStates option:selected").val();

    selectCountry = $("#dropDownCountryForStates option:selected");
    countryId = selectCountry.val();
    countryName = selectCountry.text();

    jsonDataState = { id: stateId, name: stateName, country: {id :countryId, name: countryName}};

    $.ajax({
        url: url,
        type: 'POST',
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName,csrfValue);
        },
        data: JSON.stringify(jsonDataState),
        contentType: 'application/json'
    }).done(function (stateId) {
        $("#dropDownStates option:selected").text(stateName);
        alert(stateId + " has been update");
    }).fail(function () {
        alert("fail");
    })
}

function deleteStateByCountry() {
    stateId = $("#dropDownStates option:selected").val();
    url = contextPath + "states/delete/" + stateId;

    $.ajax({
        url: url,
        type: 'DELETE',
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName,csrfValue);
        },
    }).done(function() {
        $("#dropDownStates option[value='" + stateId + "']").remove();
        changeFormStateToNew();
        alert(stateId + " has been delete");
    }).fail(function() {
        alert("fail");
    });
}

function changeFormStateToNew() {
    fieldStateName.val("").focus();

    buttonAddState.val("Add");
    buttonUpdateState.prop("disabled",true);
    buttonDeleteState.prop("disabled",true);

    labelStateName.text("State/Province Name:");
}