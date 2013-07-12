package models;

import play.*;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class AppointmentType extends Model {
    
	@Required
	public String appointmentType;
	
	public String toString()
	{
		return appointmentType;
	}
}
