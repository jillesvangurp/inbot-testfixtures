package io.inbot.testfixtures;

import java.util.Locale;

public class Person {
    private final String firstName;
    private final String lastName;
    private final String company;

    public Person(String firstName, String lastName, String company) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.company = company;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    public String getCompany() {
        return company;
    }

    public String getDomainName() {
        return getCompany().toLowerCase(Locale.ENGLISH).replaceAll("[^a-zA-Z0-9]*", "") + ".com";
    }

    public String getUserName() {
        // Galen Ullrich -> galenullrich, avoid having the string null
        return (getFirstName()+"."+getLastName()).toLowerCase(Locale.ENGLISH).replaceAll("[^a-zA-Z0-9]*", "").replaceAll("null", "n_ll");
    }

    public String getEmail() {
        return getUserName()+"@"+getDomainName();
    }

    @Override
    public String toString() {
        return "Person [ firstName=" + firstName + ", lastName=" + lastName + ", company=" + company + ", fullName()="
                + getFullName() + ", company()=" + getCompany() + ", domainName()=" + getDomainName()
                + ", userName()=" + getUserName() + ", email()=" + getEmail() + " ]";
    }
    
}
