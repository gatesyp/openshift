import fitbit

#This is the Fitbit URL to use for the API call
FitbitURL = "https://api.fitbit.com/1/user/-/profile.json"

#Use this URL to refresh the access token
TokenURL = "https://api.fitbit.com/oauth2/token"

#Get and write the tokens from here
IniFile = "tokens.txt"

OAuthTwoClientID = "227SK8"
ClientOrConsumerSecret = "8eb851b66c317ce13ed9b9e6d7d5304e"
#Open the file
FileObj = open(IniFile,'r')

#Read first two lines - first is the access token, second is the refresh token
AccToken = FileObj.readline()
RefToken = FileObj.readline()

#Close the file
FileObj.close()
