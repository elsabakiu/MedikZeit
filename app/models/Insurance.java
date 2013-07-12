package models;

import play.*;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class Insurance extends Model {
	

	public String insurance;

	public String toString()
	{
		return insurance;
	}
}
