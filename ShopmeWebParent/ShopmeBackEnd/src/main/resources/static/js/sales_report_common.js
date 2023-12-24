//sales Report common
var MILLISECONDS_A_DAY = 24 * 60 * 60 * 1000;
function setupButtonHandlers(reportType,callbackFunction) {
    $(".button-sales-by" + reportType).on("click",function () {
        $('.button-sales-by' + reportType).each(function (e) {
            $(this).removeClass('btn-info').addClass('btn-light');
        })
        $(this).removeClass('btn-light').addClass('btn-info');
        period = $(this).attr("period");

        if (period) {
            callbackFunction(period);
            $("#divCustomDateRange" + reportType).addClass("d-none")
        } else {
            $("#divCustomDateRange" + reportType).removeClass("d-none")
        }
    })

    initCustomDateRange(reportType);
    $("#buttonViewReportByDateRange" + reportType).on("click",function (e) {
        validateDateRange(reportType,callbackFunction);
    })
}
function validateDateRange(reportType,callbackFunction) {
    startDateField = document.getElementById('startDate' + reportType);
    startDateField.setCustomValidity("");
    days = calculateDays(reportType);
    if (days >= 7 && days <= 30) {
        callbackFunction("custom");
    } else {
        startDateField.setCustomValidity("Thời gian chỉ được tìm kiếm trong tháng từ 7 ngày trở lên và bé hơn 30");
        startDateField.reportValidity();
    }
}
function calculateDays(reportType) {
    startDateField = document.getElementById('startDate' + reportType);
    endDateField = document.getElementById('endDate'  + reportType);

    startDate = startDateField.valueAsDate;
    endDate = endDateField.valueAsDate;

    differenceInMilliseconds = endDate - startDate;
    return differenceInMilliseconds / MILLISECONDS_A_DAY;
}
function initCustomDateRange(reportType) {
    startDateField = document.getElementById('startDate' + reportType);
    endDateField = document.getElementById('endDate'  + reportType);
    toDate = new Date();
    endDateField.valueAsDate = toDate;

    fromDate = new Date();
    fromDate.setDate(toDate.getDate() - 30);
    startDateField.valueAsDate = fromDate;
}

function getChartTitle(period) {
    if (period == "last_7_days") return "Sản lượng trong 7 ngày qua";
    if (period == "last_28_days") return "Sản lượng trong 28 ngày qua";
    if (period == "last_6_months") return "Sản lượng trong 6 tháng qua";
    if (period == "last_year") return "Sản lượng trong năm qua";
    if (period == "custom") return "Ngày tùy chọn"
    return "Sản lượng trong 7 ngày qua";
}

function getDenominator(period,reportType) {
    if (period == "last_7_days") return 7;
    if (period == "last_28_days") return 28;
    if (period == "last_6_months") return 6;
    if (period == "last_year") return 12;
    if (period == "custom") return calculateDays(reportType);

    return 7;
}

function setSalesAmount(period,reportType,labelTotalItem) {
    $("#textTotalGrossSales" + reportType).text( $.number(totalGrossSales,0) + " VNĐ");
    $("#textTotalNetSales" + reportType).text($.number(totalNetSales,0) + " VNĐ");

    denominator = getDenominator(period,reportType);
    $("#textAvgTotalGrossSales" + reportType).text( $.number(totalGrossSales / denominator,0) + " VNĐ");
    $("#textAvgTotalNetSales" + reportType).text($.number(totalNetSales / denominator,0) + " VNĐ");
    $("#labelTotalItems" + reportType).text(labelTotalItem);
    $("#textTotalOrders" + reportType).text(totalItems);
}

function formatChartData(data,columnIndex1,columnIndex2) {
    var formatter = new google.visualization.NumberFormat({
        prefix: prefixCurrencySymbol,
        suffix: suffixCurrencySymbol,
        decimalSymbol: decimalPointType,
        groupingSymbol: thousandsPointType,
        fractionDigits: decimalDigits
    });

    formatter.format(data,columnIndex1,columnIndex2);
    formatter.format(data,columnIndex1,columnIndex2);
}