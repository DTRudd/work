package peopleAndAnimals;
import java.io.FileInputStream;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.HashSet;
import java.util.Iterator;
import java.lang.StringBuilder;
public class PeopleAndAnimals {

	public static void main(String[] Args){
		Tuple<ArrayList<Person>,ArrayList<Animal>> things = importObjects("ExData.txt");
		ArrayList<Person> people = things.first();
		ArrayList<Animal> pets = things.second();
		setPetRelations(people,pets);
		
		HashSet<String> species = new HashSet<String>();
		HashSet<String> jobs = new HashSet<String>();
		
		for(Animal a : pets){
			species.add(a.getSpecies());
		}
		for (Person p : people){
			jobs.add(p.getJobTitle());
		}
		
		people.stream().map(p -> p.toString()).forEach(System.out::println);
		System.out.println("");
		people.stream().map(p -> petsOwned(p)).forEach(System.out::println);
		System.out.println("");
		pets.stream().map(p -> p.toString()).forEach(System.out::println);
		System.out.println("");
		species.stream().map(s -> speciesAverageSalary(people,s)).forEach(System.out::println);
		System.out.println("");
		species.stream().map(p -> speciesJobs(people,p)).forEach(System.out::println);
		System.out.println("");
		jobs.stream().map(j -> jobsSpecies(people,j)).forEach(System.out::println);
	}
	
	public static String jobsSpecies(ArrayList<Person> people, String jobTitle){
		HashSet<String> species = new HashSet<String>();
		for (Person p : people){
			if (p.getJobTitle().equals(jobTitle)){
				for(Animal a : p.getPets()){
					species.add(a.getSpecies());
				}
			}
		}
		StringBuilder sb = new StringBuilder();
		if (species.size() == 0){
			sb.append("No " + jobTitle + " owns any pets.");
		} else {
			sb.append(jobTitle + "s own ");
			Iterator<String> i = species.iterator();
			while (i.hasNext()){
				String x = i.next();
				sb.append(x + "s");
				if (i.hasNext()){
					sb.append(" and ");
				} else {
					sb.append(".");
				}
			}
		}
		return sb.toString();
	}
	public static void setPetRelations(ArrayList<Person> people, ArrayList<Animal> pets){
		for (Person p : people){
			if (p.getName().equals("Elliott")){
				Animal[] ePets = new Animal[2];
				for (Animal a : pets){
					if (a.getSpecies().equals("Tortoise")){
						ePets[0] = a;
					} else if (a.getSpecies().equals("Cat")){
						ePets[1] = a;
					}
				}
				p.setPets(ePets);
			} else if (p.getName().equals("Gareth")){
				Animal[] gPets = new Animal[2];
				for (Animal a: pets){
					if (a.getSpecies().equals("Cat")){
						gPets[0] = a;
					} else if (a.getSpecies().equals("Dolphin")){
						gPets[1] = a;
					}
				}
				p.setPets(gPets);
			}
		}
	}
	public static String speciesJobs(ArrayList<Person> people, String species){
		HashSet<String> jobs = new HashSet<String>();
		for (Person p : people){
			for (Animal a : p.getPets()){
				if (a.getSpecies().equals(species)){
					jobs.add(p.getJobTitle());
				}
			}
		}
		StringBuilder sb = new StringBuilder();
		if (jobs.size() == 0){
			sb.append("No-one owns a " + species + ".");
		} else {
			sb.append(species + "s are owned by ");
			Iterator<String> i = jobs.iterator();
			while (i.hasNext()){
				String x = i.next();
				sb.append(x + "s");
				if (i.hasNext()){
					sb.append(" and ");
				} else {
					sb.append(".");
				}
			}
		}
		return sb.toString();
	}
	
	public static int calculateAge(String dob){
		String[] fields = dob.split("/");
		GregorianCalendar curDate = new GregorianCalendar();
		if (Integer.parseInt(fields[0]) < curDate.get(Calendar.DAY_OF_MONTH) && Integer.parseInt(fields[1]) < curDate.get(Calendar.MONTH + 1 )){
			return curDate.get(Calendar.YEAR) - Integer.parseInt(fields[2]);
		} else {
			return curDate.get(Calendar.YEAR) - (1 + Integer.parseInt(fields[2]));
		}
	}
	
	public static String petsOwned(Person p){
		StringBuilder sb = new StringBuilder();
		if (p.getPets().length == 0){
			sb.append(p.getName() + " owns no pets.");
		} else {
			sb.append(p.getName() + " owns ");
			for (int ii = 0; ii < p.getPets().length; ii++){
				sb.append("a " + p.getPets()[ii].getSpecies() + " named " + p.getPets()[ii].getName());
				if (ii != p.getPets().length - 1){
					sb.append(" and ");
				} else {
					sb.append(".");
				}
			}
		}
		return sb.toString();
	}
	
	public static String speciesAverageSalary(ArrayList<Person> people, String species){
		int salSum = 0;
		int acc = 0;
		for(Person p : people){
			for(Animal a : p.getPets()){
				if (a.getSpecies().equals(species)){
					salSum += p.getSalary();
					acc++;
				}
			}
		}
		StringBuilder sb = new StringBuilder();
		if (acc != 0){
			sb.append(species + "s are owned by people with an average salary of £" + salSum / acc + ".");
		} else {
			sb.append("No-one owns a " + species + ".");
		}
		return sb.toString();
	}
	
	public static Tuple<ArrayList<Person>,ArrayList<Animal>> importObjects(String filename){
		ArrayList<Person> people = new ArrayList<Person>();
		ArrayList<Animal> animals = new ArrayList<Animal>();
		Pattern plaintext = Pattern.compile("([\\w /]+)");
		try(FileInputStream f = new FileInputStream(filename); Scanner s = new Scanner(f)){
			boolean peopleDone = false;
			ArrayList<String> tokens = new ArrayList<String>();
			while(s.hasNextLine()){
				String token = s.nextLine();
				Matcher m = plaintext.matcher(token);
				if(m.find()){
					token = m.group(1);
				}
				if (token.equals("")){
					continue;
				}
				if (!(token.equals("%")) && !(token.equals("&"))){
					tokens.add(token);
				} else {
					if (peopleDone){
						animals.add(animalify(tokens));
						tokens.clear();
					} else {
						people.add(personify(tokens));
						tokens.clear();
					}
					if (token.equals("&")){
						peopleDone = true;
					}
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		return new Tuple<ArrayList<Person>,ArrayList<Animal>>(people,animals);
	}
	
	public static Animal animalify(ArrayList<String> inp){
		return new Animal(inp.get(0),inp.get(1));
	}
	
	public static Person personify(ArrayList<String> inp){
		Animal[] shell = {};
		return new Person(inp.get(0),inp.get(1),inp.get(2),Integer.parseInt(inp.get(3)),shell);
		
	}
	
}
