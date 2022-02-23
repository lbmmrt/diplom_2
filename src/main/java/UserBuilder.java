import org.apache.commons.lang3.RandomStringUtils;

public class UserBuilder {

    private String email;
    private String password;
    private String name;

    public UserBuilder() {
    }

    public UserBuilder setRandomParams() {
        this.email = RandomStringUtils.randomAlphabetic(10) + "@" +
                RandomStringUtils.randomAlphabetic(5) + "." +
                RandomStringUtils.randomAlphabetic(2);
        this.password = RandomStringUtils.randomAlphabetic(10);
        this.name = RandomStringUtils.randomAlphabetic(10);

        return this;
    }

    public UserBuilder setEmail(String email) {
        this.email = email;

        return this;
    }

    public UserBuilder setRandomEmail() {
        this.email = RandomStringUtils.randomAlphabetic(10) + "@" +
                     RandomStringUtils.randomAlphabetic(5) + "." +
                     RandomStringUtils.randomAlphabetic(2);

        return this;
    }

    public UserBuilder setPassword(String password) {
        this.password = password;

        return this;
    }

    public UserBuilder setRandomPassword() {
        this.password = RandomStringUtils.randomAlphabetic(10);

        return this;
    }

    public UserBuilder setName(String name) {
        this.name = name;

        return this;
    }

    public UserBuilder setRandomName() {
        this.name = RandomStringUtils.randomAlphabetic(10);

        return this;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public User build() {
        return new User(email, password, name);
    }
}

