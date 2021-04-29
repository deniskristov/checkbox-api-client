package ua.in.checkbox.api.client.utils;

import ua.in.checkbox.api.client.dto.receipt.Payment;
import ua.in.checkbox.api.client.dto.report.ReportModel;
import ua.in.checkbox.api.client.dto.report.ReportPaymentsModel;

public class ReportUtils
{
    public static int getServiceIn(ReportModel report)
    {
        return report.getPayments().stream()
            .mapToInt(ReportPaymentsModel::getServiceIn)
            .sum();
    }

    public static int getServiceOut(ReportModel report)
    {
        return report.getPayments().stream()
            .mapToInt(ReportPaymentsModel::getServiceOut)
            .sum();
    }

    public static int getSellSum(ReportModel report, Payment.TYPE paymentType)
    {
        return report.getPayments().stream()
            .filter(reportPaymentsModel -> reportPaymentsModel.getType() == paymentType)
            .mapToInt(ReportPaymentsModel::getSellSum)
            .sum();
    }

    public static int getReturnSum(ReportModel report, Payment.TYPE paymentType)
    {
        return report.getPayments().stream()
            .filter(reportPaymentsModel -> reportPaymentsModel.getType() == paymentType)
            .mapToInt(ReportPaymentsModel::getReturnSum)
            .sum();
    }
}
