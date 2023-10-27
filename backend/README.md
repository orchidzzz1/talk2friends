# Backend of the app
Using Firebase for Google authentication/email, Cyclic for hosting and AWS databases, cache for verification codes

## Database schema
### Users
email (string, unique id) |  name (string) | affiliation (string) | international (bool: 1 if true else false)
### Interests
id (string: email+interest)
### Friends
id (string: email1+email2)| accepted (bool) | 
### Meetings
id (string: creator's email+datetime at creation) | title (string) | datetime (string: datetime of the event) | description (string) | location (string)
### Participations
id (string: meetingId + user's email)

AWS DynamoDB uses primary key (pk) and secondary key (sk) for querying so the table's name/type is pk and the id/email column of each table is sk

Note: use '-' in between each string concatenation

## Routes
### User Routes
/api/user/authorize: Outputs {isVerified: true, user: user object} if user exists and is verified. Else, outputs {isVerified: false} and sends verification code to user's email

/api/user/get (GET Route): Outputs {user: user object}

/api/user/add: user = {name, affiliation, international, interests[]} 

/api/user/update: name, affiliation, international

/api/user/friends/add: friendEmail

/api/user/friends/remove: friendEmail

/api/user/addInterest: interest

/api/user/removeInterest: interest

/api/user/friends/recommended (GET Route): interests[]. Outputs {users: [{userEmail: list of interests in common}, ]}

/api/user/recommend: email, userName

/api/user/sendVerification: 

/api/user/verify: code. Outputs {isVerified: true/false}

### Meeting routes
/api/meeting/get (GET Route): Outputs {meetings: list of meeting objects}

/api/meeting/edit: title description, datetime, location

/api/meeting/create: title, description, datetime, location

/api/meeting/remove: meetingId

/api/meeting/rsvp: meetindId

/api/meeting/cancel: meetingId


Above are the roures' inputs to be passed from front end and outputs to be received by front end. Only routes mentioned as (GET Route) are GET, the rest are POST routes
