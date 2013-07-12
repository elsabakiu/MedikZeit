package models;

import play.*;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;

import org.joda.time.DateTime;

import java.util.*;

@Entity
public class AppointmentStatus extends Model{
	
	@Required
	public String status;
	
	public String toString()
	{
		return status;
	}
}
