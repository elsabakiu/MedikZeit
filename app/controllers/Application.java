package controllers;

import play.*;
import play.mvc.*;

import java.util.*;
import java.util.Calendar;
import java.net.*;
import java.io.*;

import models.*;

public class Application extends Controller {
	    
		public static void index() {
			 render();
		}

		
		/**
		 * Lists the doctors based on the search criteria 
		 * @param doctorSpecialization
		 * @param city
		 * @param insurance
		 */
 		public static void listDoctors(String doctorSpecialization, String city, String insurance, Date startDate) {
 			List<Doctor> doctors = Doctor.find("user.address.city = ?1 and user.insurance.insurance = ?2 and docSpecialization.specializationName = ?3", city, insurance, doctorSpecialization).fetch();
 			
 			
 			/**  Pass a variable calendar to display the current week 
 			 * starting from the current day */
 			if(startDate == null) 
 			startDate = new Date();
 			Calendar calendar = Calendar.getInstance();
 			calendar.setTime(startDate);
 			
 			/** 
 			 * Calculate for each doctor the free time slots based on 
 			 * the events they have saved in DB and the active appointments
 			 */
 			ArrayList<Calendar[][]> freeSlotsForAllDoctors = new ArrayList<Calendar[][]>();
 			for(int i = 0; i < doctors.size(); i++)
 			{
 				freeSlotsForAllDoctors.add(i, calculateFreeTimeSlotsForDoctor(doctors.get(i).id, startDate));
 			}
 			
 			render(doctors, freeSlotsForAllDoctors, calendar, doctorSpecialization, city, insurance);
    	}
 		
 		/**
 		 * Calculates the free time slots for the given doctor
 		 * @param id - Id of the doctor for whom we are calculating the free time slots
 		 * @param startDate - Start date of the week for which the calculation is done
 		 * @return an array that for each day of the current week and for each 30 min 
 		 * interval of the day gives the availability of the doctor
 		 */
 		public static Calendar[][] calculateFreeTimeSlotsForDoctor(long id, Date startDate)
 		{
 			Calendar calendar = Calendar.getInstance();
 			calendar.setTime(startDate);
 			calendar.set(Calendar.HOUR_OF_DAY, 8);
 			calendar.set(Calendar.MINUTE, 1);
 			
 			Calendar endOfWeekCal = Calendar.getInstance();
 			endOfWeekCal.setTime(startDate);
 			endOfWeekCal.add(Calendar.DAY_OF_MONTH, 8);
 			//Date endOfWeek = endOfWeekCal.getTime();
 			
 			//List<Event> events = Event.find(" doctor.id = ?1 AND start >= ?2 AND end < ?3 ", id, startDate, endOfWeek).fetch();
 			List<Event> events = Event.find(" doctor.id = ?1 AND start >= ?2  ", id, startDate).fetch();

 			//For 30 min slots - Bound 19
 			Calendar[][] freeSlots = new Calendar[7][10];
 			for(int i = 0; i < 7; i++)
 			{
 				for(int j = 0; j < 10; j++)
 				{
 					boolean isBusy = false;
 					int k = 0;
 				
 					while(!isBusy && k < events.size())
 					{
 						System.out.println(events.get(k).start + " " + calendar.getTime() + " " + events.get(k).end );
 						if(calendar.getTime().compareTo(events.get(k).start) >= 0 && calendar.getTime().compareTo(events.get(k).end) < 0)
 							isBusy = true;
 						k++;
 					}
 					
 					if(!isBusy && calendar.getTime().compareTo(new Date()) > 0)
 					{
 						Calendar newCal = Calendar.getInstance();
 			 			newCal.setTime(calendar.getTime());
 			 			newCal.add(Calendar.MINUTE, -1);
 						freeSlots[i][j] = newCal;
 					}
 					else
 						freeSlots[i][j] = null;
 					
 					calendar.add(Calendar.MINUTE, 60);
 				}
 				
 				/**
 				 * We go to the next day of the weak starting from 8:00 o'clock
 				 */
 				calendar.set(Calendar.HOUR_OF_DAY, 8);
 				calendar.set(Calendar.MINUTE, 1);
 				calendar.add(Calendar.DAY_OF_MONTH, 1);
 			}
 			
 			return freeSlots;
 		}
 		


}
