package models;

import play.*;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;
import org.joda.time.DateTime;
import java.util.*;

@Entity
public class Doctor extends Model {
	
	@Required
	public String docClinicName;
	public String docPhoto;
    @OneToOne
    public User user;
    
    @ManyToOne
    public DoctorSpecialization docSpecialization;
    
    public String toString()
    {
    	return user.firstname + " " + user.lastname;
    }
}
 
