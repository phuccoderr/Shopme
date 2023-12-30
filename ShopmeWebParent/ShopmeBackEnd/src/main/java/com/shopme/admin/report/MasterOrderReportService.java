package com.shopme.admin.report;

import com.shopme.admin.order.OrderRepository;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.OrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class MasterOrderReportService extends AbtractReportService {

    @Autowired private OrderRepository repo;




    protected List<ReportItem> getReportDataByDateRangeInternal(Date startTime, Date endTime, ReportType reportType) {
        List<Order> listOrders = repo.findByOrderTimeBetWeen(startTime,endTime);
//        printRawData(listOrders);

        List<ReportItem> listReportItems = createReportData(startTime,endTime,reportType);


        calculateSalesForReportData(listOrders,listReportItems);
//        printReportData(listReportItems);
        return listReportItems;
    }

    private List<ReportItem> createReportData(Date startTime, Date endTime, ReportType reportType) {
        List<ReportItem> listReportItem = new ArrayList<>();

        Calendar startDate = Calendar.getInstance();
        startDate.setTime(startTime);

        Calendar endDate = Calendar.getInstance();
        endDate.setTime(endTime);

        Date currentDate = startDate.getTime();
        String dateString = dateFormat.format(currentDate);

        listReportItem.add(new ReportItem(dateString));
        do {
            if (reportType.equals(ReportType.DAY)) {
                startDate.add(Calendar.DAY_OF_MONTH,1);
            } else if (reportType.equals(ReportType.MONTH)) {
                startDate.add(Calendar.MONTH,1);
            }

            currentDate = startDate.getTime();
            dateString = dateFormat.format(currentDate);

            listReportItem.add(new ReportItem(dateString));
        } while (startDate.before(endDate));

        return listReportItem;
    }

    private void calculateSalesForReportData(List<Order> listOrders, List<ReportItem> listReportItems) {
        listOrders.forEach( order -> {
            String orderDateString = dateFormat.format(order.getOrderTime());

            ReportItem reportItem = new ReportItem(orderDateString);

            int itemIndex = listReportItems.indexOf(reportItem);

            if (itemIndex >= 0) {
                reportItem = listReportItems.get(itemIndex);

                reportItem.addGrossSales(order.getTotal());

                reportItem.addNetSales(order.getSubtotal() - order.getProductCost());
                reportItem.increaseOrderCount();

            }
        });
    }




    private void printRawData(List<Order> listOrders) {
        listOrders.forEach(order -> {
            System.out.printf("%-3d | %s | %10.2f | %10.2f \n"
                ,order.getId()
                ,order.getOrderTime()
                ,order.getSubtotal()
                ,order.getTotal());
        });
    }

    private void printReportData(List<ReportItem> listReportItems) {
        listReportItems.forEach(item -> {
            System.out.printf("%s, %10.2f, %10.2f, %d \n"
                    ,item.getIdentifier()
                    ,item.getGrossSales()
                    ,item.getNetSales()
                    ,item.getOrdersCount());
        });
    }





}
