import base64
import urllib2
import urllib
import sys
import json
import os
from sql import SQLConnection

class fitbit():
  
  #This is the Fitbit URL to use for the API call
  date=""
  FitbitURL = "https://api.fitbit.com/1/user/-/profile.json"
  FitbitActivityURL = "https://api.fitbit.com/1/user/-/activities/date/2016-08-25.json"
  FitbitSleepURL = "https://api.fitbit.com/1/user/-/sleep/date/2016-07-11.json"

  #Use this URL to refresh the access token
  TokenURL = "https://api.fitbit.com/oauth2/token"

  #Get and write the tokens from here
  # IniFile = "tokens.txt"

  #From the developer site
  OAuthTwoClientID = "227SK8"
  ClientOrConsumerSecret = "8eb851b66c317ce13ed9b9e6d7d5304e"

  #Some contants defining API error handling responses
  TokenRefreshedOK = "Token refreshed OK"
  ErrorInAPI = "Error when making API call that I couldn't handle"

  def set_date(self,date):
    self.date=date

  #Get the config from the config file.  This is the access and refresh tokens
  def GetConfig(self,username):
    print "Reading from the config file"
    db=SQLConnection()
    #Open the file
    # FileObj = open(IniFile,'r')

    # #Read first two lines - first is the access token, second is the refresh token
    # AccToken = FileObj.readline()
    # RefToken = FileObj.readline()
    AccToken=db.get_fitbit_acc_token(username)
    RefToken=db.get_fitbit_ref_token(username)
    # #Close the file
    # FileObj.close()

    #See if the strings have newline characters on the end.  If so, strip them
    if (AccToken.find("\n") > 0):
      AccToken = AccToken[:-1]
    if (RefToken.find("\n") > 0):
      RefToken = RefToken[:-1]

    #Return values
    return AccToken, RefToken

  # def WriteConfig(self,AccToken,RefToken):
  #   print "Writing new token to the config file"
  #   print "Writing this: " + AccToken + " and " + RefToken

  #   #Delete the old config file
  #   #os.remove(IniFile)

  #   #Open and write to the file
  #   # FileObj = open(IniFile,'w')
  #   # FileObj.write(AccToken + "\n")
  #   # FileObj.write(RefToken + "\n")
  #   # FileObj.close()

  #Make a HTTP POST to get a new
  def GetNewAccessToken(self,RefToken,username):
    print "Getting a new access token"

    #Form the data payload
    BodyText = {'grant_type' : 'refresh_token',
                'refresh_token' : RefToken}
    #URL Encode it
    BodyURLEncoded = urllib.urlencode(BodyText)
    print "Using this as the body when getting access token >>" + BodyURLEncoded

    #Start the request
    tokenreq = urllib2.Request(self.TokenURL,BodyURLEncoded)

    #Add the headers, first we base64 encode the client id and client secret with a : inbetween and create the authorisation header
    tokenreq.add_header('Authorization', 'Basic ' + base64.b64encode(self.OAuthTwoClientID + ":" + self.ClientOrConsumerSecret))
    tokenreq.add_header('Content-Type', 'application/x-www-form-urlencoded')

    #Fire off the request
    try:
      db=SQLConnection()
      tokenresponse = urllib2.urlopen(tokenreq)

      #See what we got back.  If it's this part of  the code it was OK
      FullResponse = tokenresponse.read()

      #Need to pick out the access token and write it to the config file.  Use a JSON manipluation module
      ResponseJSON = json.loads(FullResponse)

      #Read the access token as a string
      NewAccessToken = str(ResponseJSON['access_token'])
      NewRefreshToken = str(ResponseJSON['refresh_token'])
      #Write the access token to the ini file
      db.update_fitbit_acc_token(username,NewAccessToken,NewRefreshToken)
      #self.WriteConfig(NewAccessToken,NewRefreshToken) #HERE YOU GO ROOS LOOK HERE BUDDY LOOK LOOK LOOK ################)

      print "New access token output >>> " + FullResponse
    except urllib2.URLError as e:
      #Gettin to this part of the code means we got an error
      print "An error was raised when getting the access token.  Need to stop here"
      print e.code
      print e.read()
      sys.exit()

  #This makes an API call.  It also catches errors and tries to deal with them
  def MakeAPICall(self,InURL,AccToken,RefToken,username):
    #Start the request
    req = urllib2.Request(InURL)

    #Add the access token in the header
    req.add_header('Authorization', 'Bearer ' + AccToken)

    print "I used this access token " + AccToken
    #Fire off the request
    try:
      #Do the request
      response = urllib2.urlopen(req)
      #Read the response
      FullResponse = response.read()

      #Return values
      return True, FullResponse
    #Catch errors, e.g. A 401 error that signifies the need for a new access token
    except urllib2.URLError as e:
      print "Got this HTTP error: " + str(e.code)
      HTTPErrorMessage = e.read()
      print "This was in the HTTP error message: " + HTTPErrorMessage
      #See what the error was
      if (e.code == 401) and (HTTPErrorMessage.find("Access token expired") > 0):
        print "e.code was 401!"
        self.GetNewAccessToken(RefToken,username)
        return False, self.TokenRefreshedOK
      #Return that this didn't work, allowing the calling function to handle it
      return False, self.ErrorInAPI

  #Main part of the code
  #Declare these global variables that we'll use for the access and refresh tokens
  def get_user_data(self,username,type):

    AccessToken = ""
    RefreshToken = ""

    print "Fitbit API Test Code"

    #Get the config
    AccessToken, RefreshToken = self.GetConfig(username)

    #Make the API call
    if type=="fitness":
      APICallOK, APIResponse = self.MakeAPICall(self.FitbitActivityURL, AccessToken, RefreshToken,username)
    if type=="sleep":
      APICallOK, APIResponse = self.MakeAPICall(self.FitbitSleepURL, AccessToken, RefreshToken,username)

    if APICallOK:
      parsed = json.loads(APIResponse)
      result= json.dumps(parsed, indent=4, sort_keys=True)
      return parsed
      # print parsed["summary"]["steps"]
    else:
      if (APIResponse == self.TokenRefreshedOK):
        print "Refreshed the access token.  Can go again"
      else:
       print self.ErrorInAPI

  def make_score_fitness(self,username):
    type="fitness"
    result=self.get_user_data(username,type)
    steps= result['summary']['steps']
    highly_active_min=result['summary']['veryActiveMinutes']
    fairly_active_min=result['summary']['fairlyActiveMinutes']
    lightly_active_min=result['summary']['lightlyActiveMinutes']
    sed_min=result['summary']['sedentaryMinutes']
    score = highly_active_min+(fairly_active_min*.5)+(lightly_active_min*.25)-(sed_min*.05)  #calculate their score
    score=score*10
    return score


  def add_events_fitness(self,username):

    db=SQLConnection()
    users=db.get_all_users()
    for username in users:
      petname=db.find_pet_of_user(username)
      category="fitness"
      xp_change=self.make_score_fitness(username)
      type1=1

      if xp_change<=-300:
        message="You have seriously harmed " +petname + " from neglagence yesterday. Make it up to " + petname +" by exercising today"
        pet_status="harmed_exercise"
      elif xp_change<=100:
        message= petname+" is sad from the lack of exercise yesterday"
        pet_status="low_exercise"

      elif xp_change<=300:
        message=petname+" got some exercise yesterday, but is still eager to play today "
        pet_status="avg_exercise"
      else:
        message=petname+" got plenty of exercise yesterday and as a result he is very happy!"
        pet_status_="good_exercise"

      db.insert_fitbit_events(username,category,pet_status,xp_change,message,type1)

    def make_score_sleep(self,username):
      type="sleep"
      result=self.get_user_data(username,type)
      print result


fitbit("hi")

#fitbit().make_score_sleep("rwr21")
