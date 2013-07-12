package models;

import play.*;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;

import org.joda.time.DateTime;

import java.util.*;

@Entity
public class DoctorSpecialization  extends Model {
    

	public String specializationName;
	
	public String toString()
	{
		return specializationName;
	}
}

