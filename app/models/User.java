package models;

import play.*;
import play.data.validation.*;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;
import play.libs.Codec;
 
@Entity
public class User extends Model {
 
    @Email(message = "invalid.email")
    @Required(message = "no.email")
    public String email;
    
    @Password
    public String password;
    
    @Required(message = "no.firstname")
    public String firstname;
    
    @Required(message = "no.lastname")
    public String lastname;
    
    public boolean gender;
    public String phoneNumber;
    
    @ManyToOne
    public Insurance insurance;

    @ManyToOne
    public UserType userType;
    
    @OneToOne
    public Address address;

    public void setPassword(String password) {
    	this.password = password;
    	//this.passwordHash = Codec.hexMD5(password);
    }
    
    public static boolean isValidLogin(String email, String password) {
    	
    	return (count("email=? AND password=?", email, password) == 1);
    }
    
    public String toSring()
    {
    	return  firstname +  " " + lastname;
    }
}
