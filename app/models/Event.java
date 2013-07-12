package models;

import play.*;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;

import java.util.*;

@Entity
public class Event extends Model {
    
	@Required
	public Date start;
	@Required
	public Date end;
	@Required
	public String title;
	@Required
	public String body;
	@ManyToOne
	public Doctor doctor;
	@OneToOne
	public User patient;
	@ManyToOne
	public AppointmentStatus status;
	
	public Event(Date start, Date end, String title, String desc, Doctor doctor)
	{
		this.start = start;
		this.end = end;
		this.title = title;
		this.body = desc;
		this.doctor = doctor;
	}
	
	public Event(Date start, Date end, String title, String desc, Doctor doctor, User patient)
	{
		this.start = start;
		this.end = end;
		this.title = title;
		this.body = desc;
		this.doctor = doctor;
		this.patient = patient;
		//this.status = (AppointmentStatus) AppointmentStatus.find(" status = 1? ", "Active").fetch().get(0);
	}
	
	public String toString()
	{
		return title + " - Start:" + start + " End:" + end;  
	}
}
