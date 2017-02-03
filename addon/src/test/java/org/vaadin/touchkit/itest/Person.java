package org.vaadin.touchkit.itest;

import java.util.Arrays;

public class Person implements Comparable<Person> {

    private String firstName;
    private String lastName;

    public Person(String string, String string2) {
        firstName = string;
        lastName = string2;
    }

    @Override
    public int compareTo(Person o) {
        int compareTo = lastName.compareTo(o.lastName);
        if (compareTo == 0) {
            compareTo = firstName.compareTo(o.firstName);
        }
        return compareTo;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName
     *            the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName
     *            the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    static Person[] teststuff = new Person[] { new Person("Tyrone", "Kim"),
            new Person("Tomas", "Lawson"), new Person("Brian", "Gutierrez"),
            new Person("Bob", "Weber"), new Person("Owen", "Schultz"),
            new Person("Amelia", "Owen"), new Person("Olive", "Clark"),
            new Person("Rafael", "Armstrong"), new Person("Debra", "Haynes"),
            new Person("Karla", "Waters"), new Person("Victoria", "Lloyd"),
            new Person("Tanya", "Castro"), new Person("Rhonda", "Hamilton"),
            new Person("Jan", "Walsh"), new Person("Molly", "Bates"),
            new Person("Arturo", "Horton"), new Person("Ross", "Wolfe"),
            new Person("Audrey", "Paul"), new Person("Marsha", "Bradley"),
            new Person("Marian", "May"), new Person("Gene", "Price"),
            new Person("Gary", "Adkins"), new Person("Floyd", "Kelly"),
            new Person("Doris", "Graves"), new Person("Bessie", "Cook"),
            new Person("Angela", "Hampton"), new Person("Sandy", "Schmidt"),
            new Person("Dianna", "Tyler"), new Person("Clay", "Williams"),
            new Person("Dale", "Morgan"), new Person("Ryan", "Hart"),
            new Person("Ollie", "Pena"), new Person("Lucia", "Underwood"),
            new Person("Donna", "Vega"), new Person("Bryan", "Parsons"),
            new Person("Willard", "Lucas"), new Person("Chester", "Potter"),
            new Person("Roberta", "Curtis"),
            new Person("Wendell", "Mclaughlin"), new Person("Tara", "Davis"),
            new Person("Stacy", "Graham"), new Person("Carole", "Dixon"),
            new Person("Ralph", "Patrick"), new Person("Harvey", "Mccoy"),
            new Person("Josefina", "Briggs"), new Person("Fred", "Carr"),
            new Person("Verna", "Mason"), new Person("Wallace", "Morris"),
            new Person("Lori", "Dunn"), new Person("Adrian", "Gregory"),
            new Person("Emilio", "Morales"), new Person("Johnnie", "Mendez"),
            new Person("Dwight", "Russell"), new Person("Thomas", "Ross"),
            new Person("Adrienne", "Martinez"),
            new Person("Kenneth", "Brewer"),
            new Person("Eloise", "Jennings"), new Person("Jorge", "Vaughn"),
            new Person("Carlos", "Myers"), new Person("Clark", "Frazier"),
            new Person("Juana", "Powers"), new Person("Domingo", "Erickson"),
            new Person("Jessica", "Jenkins"), new Person("Nora", "Gross"),
            new Person("George", "Maxwell"), new Person("Joan", "Hernandez"),
            new Person("Marvin", "Douglas"), new Person("Silvia", "Hall"),
            new Person("Clint", "Reese"), new Person("Taylor", "Garrett"),
            new Person("Rosalie", "Thornton"),
            new Person("Arthur", "Guzman"), new Person("Rudolph", "Drake"),
            new Person("Morris", "Washington"),
            new Person("Sherman", "Rose"), new Person("Tasha", "Bell"),
            new Person("Hannah", "Ortega"), new Person("Sara", "Chambers"),
            new Person("Betty", "Hicks"), new Person("Ruby", "Perez"),
            new Person("Vickie", "Stevens"), new Person("Lydia", "Ingram"),
            new Person("Scott", "Ortiz"), new Person("Lindsey", "Johnston"),
            new Person("Gertrude", "Nelson"),
            new Person("Donald", "Maldonado"), new Person("Amy", "Berry"),
            new Person("Faye", "Fields"), new Person("Iris", "Austin"),
            new Person("Carol", "Porter"), new Person("Cecilia", "Phelps"),
            new Person("Archie", "Pierce"), new Person("Steven", "Ray"),
            new Person("Freda", "Patterson"), new Person("Marc", "Park"),
            new Person("Corey", "Mcbride"), new Person("Terry", "Nguyen"),
            new Person("Horace", "Baker"), new Person("Randy", "Green"),
            new Person("Timmy", "Flowers")

    };

    static {
        Arrays.sort(teststuff);
    }

    public static Person[] getTestPersons() {
        return teststuff;
    }

}
