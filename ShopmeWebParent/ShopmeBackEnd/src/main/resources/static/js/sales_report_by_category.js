//Sales report by category
var data;
var chartOptions;




$(document).ready(function () {
    setupButtonHandlers("_category",loadSalesReportByDateForCategory);
})


function loadSalesReportByDateForCategory(period) {
    if (period == "custom") {
        startDate = $("#startDate_category").val();
        endDate = $("#endDate_category").val();
        requestUrl = contextPath + "report/category/" + startDate + "/" + endDate ;
    } else {
        requestUrl = contextPath + "report/category/" + period ;
    }
    console.log(requestUrl)
    $.get(requestUrl,function (reportJSON) {
        prepareChartDataForSaleReportByCategory(reportJSON);
        customizeChartForSaleReportByCategory();
        formatChartData(data,1,2)
        drawChartForSalesReportByCategory();
        setSalesAmount(period,'_category',"Tổng Sản Phẩm")

    })


}

function prepareChartDataForSaleReportByCategory(reportJSON) {
    data = new google.visualization.DataTable();
    data.addColumn('string','Category');
    data.addColumn('number','Tổng doanh thu');
    data.addColumn('number','Lợi nhuận');

    totalGrossSales = 0.0;
    totalNetSales= 0.0;
    totalItems = 0;

    $.each(reportJSON,function (index,reportItem) {
       data.addRows([[reportItem.identifier,reportItem.grossSales,reportItem.netSales]]);
       totalGrossSales += parseFloat(reportItem.grossSales);
       totalNetSales += parseFloat(reportItem.netSales);
       totalItems += parseInt(reportItem.productsCount);
    });
    console.log(totalItems)
}
function customizeChartForSaleReportByCategory() {
    chartOptions = {
        height: 360,
        legend: {
            position: 'right'
        }
    };


}
function drawChartForSalesReportByCategory() {
    var salesChart =
        new google.visualization.PieChart(document.getElementById('chart_sales_by_category'));
    salesChart.draw(data,chartOptions);


}




