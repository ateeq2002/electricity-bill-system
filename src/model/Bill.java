package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Bill {
    private int billId;
    private int consumerId;
    private String month;
    private BigDecimal unitsConsumed;
    private BigDecimal ratePerUnit;
    private BigDecimal totalAmount;
    private LocalDate dueDate;
    private String status;

    public Bill() {}

    public Bill(int billId, int consumerId, String month, BigDecimal unitsConsumed,
                BigDecimal ratePerUnit, BigDecimal totalAmount, LocalDate dueDate, String status) {
        this.billId        = billId;
        this.consumerId    = consumerId;
        this.month         = month;
        this.unitsConsumed = unitsConsumed;
        this.ratePerUnit   = ratePerUnit;
        this.totalAmount   = totalAmount;
        this.dueDate       = dueDate;
        this.status        = status;
    }

    public int        getBillId()                           { return billId; }
    public void       setBillId(int billId)                 { this.billId = billId; }

    public int        getConsumerId()                       { return consumerId; }
    public void       setConsumerId(int consumerId)         { this.consumerId = consumerId; }

    public String     getMonth()                            { return month; }
    public void       setMonth(String month)                { this.month = month; }

    public BigDecimal getUnitsConsumed()                    { return unitsConsumed; }
    public void       setUnitsConsumed(BigDecimal u)        { this.unitsConsumed = u; }

    public BigDecimal getRatePerUnit()                      { return ratePerUnit; }
    public void       setRatePerUnit(BigDecimal r)          { this.ratePerUnit = r; }

    public BigDecimal getTotalAmount()                      { return totalAmount; }
    public void       setTotalAmount(BigDecimal t)          { this.totalAmount = t; }

    public LocalDate  getDueDate()                          { return dueDate; }
    public void       setDueDate(LocalDate dueDate)         { this.dueDate = dueDate; }

    public String     getStatus()                           { return status; }
    public void       setStatus(String status)              { this.status = status; }

    @Override
    public String toString() {
        return "Bill{billId=" + billId + ", month='" + month + "', total=" + totalAmount + ", status='" + status + "'}";
    }
}
