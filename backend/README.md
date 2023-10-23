# Backend of the app
Using Firebase for Google authentication, Cyclic for hosting and AWS databases

## Database schema
### Users
email (string, unique id) |  name (string) | affiliation (string) | international (bool: 1 if true else false)
### Interests
id (string: email+interest)
### Friends
id (string: email1+email2)| accepted (bool) | 
### Codes
email (string: unique id) | code (int) | verified (bool)
### Meetings
id (string: creator's email+datetime at creation) | title (string) | datetime (string: datetime of the event) | description (string) | location (string)
### Participations
id (string: meetingId + user's email)

AWS DynamoDB uses primary key (pk) and secondary key (sk) for querying so the table's name/type is pk and the id/email column of each table is sk

Note: use '-' in between each string concatenation

## Routes
