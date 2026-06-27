package model;

public class Consumer {
    private int consumerId;
    private String name;
    private String address;
    private String meterNumber;
    private String connectionType;
    private String password;
    private String status;

    public Consumer() {}

    public Consumer(int consumerId, String name, String address, String meterNumber,
                    String connectionType, String password, String status) {
        this.consumerId     = consumerId;
        this.name           = name;
        this.address        = address;
        this.meterNumber    = meterNumber;
        this.connectionType = connectionType;
        this.password       = password;
        this.status         = status;
    }

    public int    getConsumerId()                       { return consumerId; }
    public void   setConsumerId(int consumerId)         { this.consumerId = consumerId; }

    public String getName()                             { return name; }
    public void   setName(String name)                  { this.name = name; }

    public String getAddress()                          { return address; }
    public void   setAddress(String address)            { this.address = address; }

    public String getMeterNumber()                      { return meterNumber; }
    public void   setMeterNumber(String meterNumber)    { this.meterNumber = meterNumber; }

    public String getConnectionType()                   { return connectionType; }
    public void   setConnectionType(String ct)          { this.connectionType = ct; }

    public String getPassword()                         { return password; }
    public void   setPassword(String password)          { this.password = password; }

    public String getStatus()                           { return status; }
    public void   setStatus(String status)              { this.status = status; }

    @Override
    public String toString() {
        return name + " (" + meterNumber + ")";
    }
}
