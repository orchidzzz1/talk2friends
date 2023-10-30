/**
 * Data objects to store information
 * @param id: unique id (string) = email of creator + created datetime in milliseconds
 * @param participants: list of participants' emails (list of string)
 * @param datetime: datetime (string)
 * @param title: (string)
 * @param description: (string)
 * @param location: physical or Zoom link address (string)
 *
 */
class Meeting {
    meetingId = "";
    participants = "";
    datetime = "";
    title = "";
    description = "";
    location = "";
}
module.exports = Meeting;
