/**
 * Data objects to store information
 * @param email: unique id
 * @param userName: display name
 * @param affiliation: user's school/department
 * @param international (bool):
 * @param interests: list of interest (string)
 * @param friends: list of friends' emails (string) and their request status (bool) in which
 * 0 means pending and 1 means already friends
 * @param meetings: list of meetings' info (Meeting objects)
 *
 */
class User {
    email = "";
    userName = "";
    affiliation = ""; //
    international = false;
    interests = [];
    friends = [];
    meetings = [];
    requests = [];
}
module.exports = User;
