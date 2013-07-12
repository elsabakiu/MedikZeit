package controllers;

import java.util.*;
import java.text.SimpleDateFormat;

import com.google.gson.JsonObject;

import models.Doctor;
import models.Event;
import models.User;
import play.mvc.*;
import models.*;

public class Calendar extends Controller {

	public static Doctor doctor;
	public static User patient;
	
    public static void index() {
    	String loggedInUserEmail = session.get("loggedInUser");
    	doctor = (Doctor) Doctor.find(" user.email = ?1", loggedInUserEmail).fetch().get(0);
        render();
    }
    
    
    public static void indexPatient()
    {
    	String loggedInUserEmail = session.get("loggedInUser");
    	patient = (User) User.find(" email = ?1", loggedInUserEmail).fetch().get(0);
        render();
    }
    
    public static void getAppointmentData()
    {
    	List<Event> events = Event.find(" doctor = ?1", doctor).fetch();
    	renderJSON(events);
    }
    
    public static void saveAppointmentData(String start, String end, String title, String body)
    {
    	Date startDate;
    	Date endDate;
    	try
    	{
    		startDate = new SimpleDateFormat("yyyy MM dd HH:mm:ss").parse(start);
    	}
    	catch(Exception e)
    	{
    		startDate = new Date();
    	}
    	try
    	{
    		endDate = new SimpleDateFormat("yyyy MM dd HH:mm:ss").parse(end);
    	}
    	catch(Exception e)
    	{
    		endDate = new Date();
    	}
    	
    	Event event = new Event(startDate, endDate, title, body, doctor);
    	System.out.println(event.toString());
    	JsonObject json = new JsonObject();
    	try
    	{
    		event.save();
    		json.addProperty("saveMessage", "Event was succesfully saved!");
    	}
    	catch(Exception e)
    	{
    		json.addProperty("saveMessage", "An error ocurred!");
    	}
    	
    	json.addProperty("eventId", event.id);
    	
    	renderJSON(json.toString());
    }
    
    public static void editAppointmentData(Long id, String start, String end, String title, String body, Long idStatus)
    {
    	Date startDate;
    	Date endDate;
    	try
    	{
    		startDate = new SimpleDateFormat("yyyy MM dd HH:mm:ss").parse(start);
    	}
    	catch(Exception e)
    	{
    		startDate = new Date();
    	}
    	try
    	{
    		endDate = new SimpleDateFormat("yyyy MM dd HH:mm:ss").parse(end);
    	}
    	catch(Exception e)
    	{
    		endDate = new Date();
    	}
    	Event event = Event.findById(id);
    	event.start = startDate;
    	event.end = endDate;
    	event.title = title;
    	event.body = body;
    	event.doctor = doctor;
    	if(idStatus != -1)
    	{
    		AppointmentStatus status = (AppointmentStatus) AppointmentStatus.findById(idStatus);
    		event.status = status;
    	}
    	
    	JsonObject json = new JsonObject();
    	try
    	{
    		event.save();
    		json.addProperty("saveMessage", "Event was succesfully edited!");
    	}
    	catch(Exception e)
    	{
    		json.addProperty("saveMessage", "An error ocurred!");
    	}
    	renderJSON(json.toString());
    }
    
    
    public static void deleteAppointmentData(Long id)
    {
    	JsonObject json = new JsonObject();
    	try
    	{
    		Event.delete(" id = ? ", id);
    		json.addProperty("saveMessage", "Event was succesfully saved!");
    	}
    	catch(Exception e)
    	{
    		json.addProperty("saveMessage", "An error ocurred!");
    	}
    	renderJSON(json.toString());
    }
}
