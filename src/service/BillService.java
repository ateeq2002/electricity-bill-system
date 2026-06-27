package service;

import dao.BillDAO;
import model.Bill;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class BillService {
    private final BillDAO dao = new BillDAO();

    /** Rate per unit by connection type */
    public static BigDecimal getDefaultRate(String connectionType) {
        return switch (connectionType) {
            case "Commercial"  -> new BigDecimal("25.00");
            case "Industrial"  -> new BigDecimal("20.00");
            default            -> new BigDecimal("15.00"); // Residential
        };
    }

    public boolean generateBill(int consumerId, String month, BigDecimal units,
                                BigDecimal ratePerUnit) {
        if (units.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Units consumed must be greater than 0.");

        BigDecimal total   = units.multiply(ratePerUnit);
        LocalDate dueDate  = LocalDate.now().plusDays(30);

        Bill b = new Bill(0, consumerId, month, units, ratePerUnit, total, dueDate, "Pending");
        return dao.addBill(b);
    }

    public boolean updateBill(Bill b)                   { return dao.updateBill(b); }
    public boolean deleteBill(int billId)               { return dao.deleteBill(billId); }
    public boolean markAsPaid(int billId)               { return dao.updateBillStatus(billId, "Paid"); }
    public boolean markAsOverdue(int billId)            { return dao.updateBillStatus(billId, "Overdue"); }

    public List<Bill> getAllBills()                      { return dao.getAllBills(); }
    public List<Bill> getBillsByConsumer(int id)        { return dao.getBillsByConsumer(id); }
    public Bill       getBillById(int id)               { return dao.getBillById(id); }
    public int        countPendingBills()               { return dao.countPendingBills(); }
}
