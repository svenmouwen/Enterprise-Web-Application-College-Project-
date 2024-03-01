import users from "@/assets/dummy-user.json";

export class User {
    // Attributes
    username;
    location;
    firstname;
    lastname;
    emailaddres;
    rights;
    //location/vestiging (do we add this?)

    constructor(username, location, firstname, lastname, emailaddres, rights) {
        this.username = username;
        this.location = location;
        this.firstname = firstname;
        this.lastname = lastname;
        this.emailaddres = emailaddres;
        this.rights = rights;
    }

    static generateUsers() {
        const numberOfUsersToShow = 10;
        const arrayOfUsers = [];

        for (const userData of users.slice(0, numberOfUsersToShow)) {
            const { Username, Location, Firstname, Lastname, Emailaddress, Rights } = userData;
            arrayOfUsers.push(new User(Username, Location, Firstname, Lastname, Emailaddress, Rights));
        }

        return arrayOfUsers;
    }
}
