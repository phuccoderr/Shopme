//Sales report by date
var data;
var chartOptions;




$(document).ready(function () {
    setupButtonHandlers("_date",loadSalesReportByDate);
})


function loadSalesReportByDate(period) {
    if (period == "custom") {
        startDate = $("#startDate_date").val();
        endDate = $("#endDate_date").val();
        requestUrl = contextPath + "report/sales_by_date/" + startDate + "/" + endDate ;
    } else {
        requestUrl = contextPath + "report/sales_by_date/" + period ;
    }
    $.get(requestUrl,function (reportJSON) {
        prepareChartDataForSaleReportByDate(reportJSON);
        customizeChartForSaleReportByDate(period);
        formatChartData(data,1,2)
        drawChart();
        setSalesAmount(period,'_date',"Tổng đơn hàng")

    })


}

function prepareChartDataForSaleReportByDate(reportJSON) {
    data = new google.visualization.DataTable();
    data.addColumn('string','Date');
    data.addColumn('number','Tổng doanh thu');
    data.addColumn('number','Lợi nhuận');
    data.addColumn('number','Đơn hàng');

    totalGrossSales = 0.0;
    totalNetSales= 0.0;
    totalItems = 0;

    $.each(reportJSON,function (index,reportItem) {
       data.addRows([[reportItem.identifier,reportItem.grossSales,reportItem.netSales,reportItem.ordersCount]]);
       totalGrossSales += parseFloat(reportItem.grossSales);
       totalNetSales += parseFloat(reportItem.netSales);
       totalItems += parseInt(reportItem.ordersCount);
    });
}
function customizeChartForSaleReportByDate(period) {
    chartOptions = {
        title: getChartTitle(period),
        'height': 360,
        legend: {position: 'top'},

        series: {
            0: {targetAxisIndex: 0},
            1: {targetAxisIndex: 0},
            2: {targetAxisIndex: 1},
        },

        vAxes: {
            0: {title: 'Sản lượng bán hàng',format: 'currency'},
            1: {title: 'Số lượng đơn đặt hàng'}
        }
    }


}
function drawChart() {
    var salesChart =
        new google.visualization.ColumnChart(document.getElementById('chart_sales_by_date'));
    salesChart.draw(data,chartOptions);


}




