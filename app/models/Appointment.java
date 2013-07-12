package models;

import play.*;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;

import org.joda.time.DateTime;

import java.sql.Time;
import java.util.*;

@Entity
public class Appointment extends Model {
	
	@Required
	public Date appointmentStartTime;
	@Required
	public Date appointmentEndTime;
	@ManyToOne
	public AppointmentStatus status;
	@ManyToOne
	public AppointmentType appointmentType;
	@ManyToOne
	public User patient;
	@ManyToOne
	public Doctor doctor;

	
	public Appointment(Date appointmentStartTime, User patient, Doctor doctor)
	{
		this.appointmentStartTime = appointmentStartTime;
		this.status = (AppointmentStatus) AppointmentStatus.find("status = ?1 ", "Active").fetch().get(0);
		this.appointmentType = (AppointmentType) AppointmentType.find("appointmentType = ?1 ", "Consultation").fetch().get(0);
		this.patient = patient;
		this.doctor = doctor;
		setAppointmentEndTime();
	}
	
	
	public String toString()
	{
		return "Patient: " + patient.firstname + " " + patient.lastname + " Doctor: " + doctor.user.firstname + " " + doctor.user.lastname + " Appointment time: " + appointmentStartTime; 
	}
	
	
	//Sets the end time of the appointment based on the type of appointment type
	//30 min for consultation 
	//60 min for medical appointments
	public void setAppointmentEndTime()
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(appointmentStartTime);
		
		if(appointmentType.appointmentType == "Medical")
			cal.add(Calendar.MINUTE, 60);
		else
			cal.add(Calendar.MINUTE, 30);
		appointmentEndTime = cal.getTime();
	}
}
    
