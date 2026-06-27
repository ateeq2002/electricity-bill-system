package service;

import dao.BillDAO;
import dao.PaymentDAO;
import model.Bill;
import model.Payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class PaymentService {
    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final BillDAO    billDAO    = new BillDAO();

    public boolean processPayment(int billId, BigDecimal amount, String method) {
        Bill bill = billDAO.getBillById(billId);
        if (bill == null) throw new IllegalArgumentException("Bill not found.");
        if ("Paid".equals(bill.getStatus())) throw new IllegalStateException("Bill is already paid.");

        String txnId  = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Payment p     = new Payment(0, billId, LocalDate.now(), amount, method, txnId);

        if (paymentDAO.addPayment(p)) {
            billDAO.updateBillStatus(billId, "Paid");
            return true;
        }
        return false;
    }

    public List<Payment> getAllPayments()               { return paymentDAO.getAllPayments(); }
    public List<Payment> getPaymentsByBill(int billId) { return paymentDAO.getPaymentsByBill(billId); }
    public double        getTotalRevenue()              { return paymentDAO.getTotalRevenue(); }
}
