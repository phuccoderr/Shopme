//Sales report by product
var data;
var chartOptions;




$(document).ready(function () {
    setupButtonHandlers("_product",loadSalesReportByDateForProduct);
})


function loadSalesReportByDateForProduct(period) {
    if (period == "custom") {
        startDate = $("#startDate_product").val();
        endDate = $("#endDate_product").val();
        requestUrl = contextPath + "report/product/" + startDate + "/" + endDate ;
    } else {
        requestUrl = contextPath + "report/product/" + period ;
    }
    $.get(requestUrl,function (reportJSON) {
        prepareChartDataForSaleReportByProduct(reportJSON);
        customizeChartForSaleReportByProduct();
        formatChartData(data,2,3)
        drawChartForSalesReportByProduct();
        setSalesAmount(period,'_product',"Tổng Sản Phẩm")

    })


}

function prepareChartDataForSaleReportByProduct(reportJSON) {
    data = new google.visualization.DataTable();
    data.addColumn('string','Sản Phẩm');
    data.addColumn('number','Số Lượng')
    data.addColumn('number','Tổng Doanh Thu');
    data.addColumn('number','Lợi Nhuận');

    totalGrossSales = 0.0;
    totalNetSales= 0.0;
    totalItems = 0;

    $.each(reportJSON,function (index,reportItem) {
       data.addRows([[reportItem.identifier,reportItem.productsCount,reportItem.grossSales,reportItem.netSales]]);
       totalGrossSales += parseFloat(reportItem.grossSales);
       totalNetSales += parseFloat(reportItem.netSales);
       totalItems += parseInt(reportItem.productsCount);
    });
}
function customizeChartForSaleReportByProduct() {
    chartOptions = {
        height: 360, width: '100%',
        showRowNumber: true,
        page: 'enable',
        sortColumn: 2,
        sortAscending: false
    };


}
function drawChartForSalesReportByProduct() {
    var salesChart =
        new google.visualization.Table(document.getElementById('chart_sales_by_product'));
    salesChart.draw(data,chartOptions);


}




