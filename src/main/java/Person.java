public class Person {

    private Integer id;
    private String firstname;
    private String lastname;
    private Integer age;
    private Integer gender;

    public static int MALE = 2;
    public static int FEMALE = 4;


    
    public static int idCounter = 1;

    
    public Person() {
        this.id = idCounter++;
        this.firstname = "";
        this.lastname = "";
        this.age = 0;
        this.gender = 0;
    }

    public Person(String firstname, String lastname, Integer age, Integer gender) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.gender = gender;
    }
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender.equals(Person.MALE) ? "M" : "F";
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }
}
