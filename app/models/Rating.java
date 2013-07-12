package models;

import play.*;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;

import org.joda.time.DateTime;

import java.util.*;

@Entity
public class Rating extends Model {

	@Required
	public int ratingValue;
	@Required
	public String comment;
	
	@ManyToOne
	public User patient;
    @ManyToOne
	public Doctor doctor;
    
    public String toString()
	{
		return "Patient: " + patient.firstname + " " + patient.lastname + " Doctor: " + doctor.user.firstname + " " + doctor.user.lastname + " Rating value: " + ratingValue; 
	}
	
    
}
