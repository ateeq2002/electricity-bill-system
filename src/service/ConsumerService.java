package service;

import dao.ConsumerDAO;
import model.Consumer;

import java.util.List;

public class ConsumerService {
    private final ConsumerDAO dao = new ConsumerDAO();

    public boolean addConsumer(String name, String address, String meterNumber,
                               String connectionType, String password) {
        if (name.isBlank() || meterNumber.isBlank() || password.isBlank()) {
            throw new IllegalArgumentException("Name, meter number, and password are required.");
        }
        Consumer c = new Consumer(0, name, address, meterNumber, connectionType, password, "Active");
        return dao.addConsumer(c);
    }

    public List<Consumer> getAllConsumers()                      { return dao.getAllConsumers(); }
    public Consumer getConsumerById(int id)                     { return dao.getConsumerById(id); }
    public Consumer getConsumerByMeter(String meter)            { return dao.getConsumerByMeter(meter); }
    public Consumer login(String meter, String password)        { return dao.login(meter, password); }
    public boolean updateConsumer(Consumer c)                   { return dao.updateConsumer(c); }
    public boolean deleteConsumer(int id)                       { return dao.deleteConsumer(id); }
    public List<Consumer> searchConsumers(String keyword)       { return dao.searchConsumers(keyword); }
}
