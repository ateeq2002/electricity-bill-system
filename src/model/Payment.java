package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Payment {
    private int paymentId;
    private int billId;
    private LocalDate paymentDate;
    private BigDecimal amountPaid;
    private String paymentMethod;
    private String transactionId;

    public Payment() {}

    public Payment(int paymentId, int billId, LocalDate paymentDate, BigDecimal amountPaid,
                   String paymentMethod, String transactionId) {
        this.paymentId     = paymentId;
        this.billId        = billId;
        this.paymentDate   = paymentDate;
        this.amountPaid    = amountPaid;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
    }

    public int        getPaymentId()                           { return paymentId; }
    public void       setPaymentId(int paymentId)              { this.paymentId = paymentId; }

    public int        getBillId()                              { return billId; }
    public void       setBillId(int billId)                    { this.billId = billId; }

    public LocalDate  getPaymentDate()                         { return paymentDate; }
    public void       setPaymentDate(LocalDate paymentDate)    { this.paymentDate = paymentDate; }

    public BigDecimal getAmountPaid()                          { return amountPaid; }
    public void       setAmountPaid(BigDecimal amountPaid)     { this.amountPaid = amountPaid; }

    public String     getPaymentMethod()                       { return paymentMethod; }
    public void       setPaymentMethod(String paymentMethod)   { this.paymentMethod = paymentMethod; }

    public String     getTransactionId()                       { return transactionId; }
    public void       setTransactionId(String transactionId)   { this.transactionId = transactionId; }

    @Override
    public String toString() {
        return "Payment{paymentId=" + paymentId + ", billId=" + billId +
               ", date=" + paymentDate + ", amount=" + amountPaid + "}";
    }
}
