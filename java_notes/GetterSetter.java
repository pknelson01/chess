package SimpleClassExample;

import java.util.Date;

public class GetterSetter {
    private String firstName;
    private String lastName;
    private Date birthDay;
    
    public void setFirstName(String firstName) {this.firstName = firstName;}
    public void setLastName(String lastName) {this.lastName = lastName;}
    public void setBirthDay(Date birthDay) {this.birthDay = birthDay;}
    public String getFirstName(){return firstName;}
    public String getLastName(){return lastName;}
    public Date getBirthDay(){return birthDay;}   
}
