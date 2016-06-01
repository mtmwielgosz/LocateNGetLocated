package extra;

/**
 * Created by mtmwi on 31.05.2016.
 */

public class Contact {

    private String name;
    private String phoneNumber;
    private Boolean checked = false;

    public Contact(String name, String phoneNumber, Boolean checked)
    {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.checked = checked;
    }

    public Contact()
    {
        this.name = "";
        this.phoneNumber = "";
        this.checked = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public Boolean isChecked()
    {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

}

