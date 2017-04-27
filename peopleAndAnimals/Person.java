package peopleAndAnimals;

import java.util.Collection;

public class Person{
	private String name;
	private String jobTitle;
	private String dob;
	private int salary;
	private Animal[] pets;
	
	public String getName(){
		return name;
	}
	public void setName(String inp){
		name = inp;
	}
	public String getJobTitle(){
		return jobTitle;
	}
	public void setJobTitle(String inp){
		jobTitle = inp;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public int getSalary() {
		return salary;
	}
	public void setSalary(int salary) {
		this.salary = salary;
	}
	public Animal[] getPets() {
		return pets;
	}
	public void setPets(Animal[] inp) {
		this.pets = inp;
	}
	public Person(String name, String dob, String jobTitle, int salary, Animal[] pets){
		this.name = name;
		this.jobTitle = jobTitle;
		this.dob = dob;
		this.salary = salary;
		this.pets = pets;
	}
	
	public String toString(){
		return this.getName() + " is a " + PeopleAndAnimals.calculateAge(this.getDob()) + "-year-old " + this.getJobTitle() + " who earns £" + this.getSalary() + ".";
	}
	
	public static int avgSalary(Collection<Person> group){
		int sum = 0;
		for(Person p : group){
			sum += p.salary;
		}
		return sum / group.size();
	}
}
