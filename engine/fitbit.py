import base64
import urllib2
import urllib
import sys
import json
import os
from sql import SQLConnection
import time
from datetime import datetime, timedelta
class fitbit():
  
  #This is the Fitbit URL to use for the API call
  date=""
  d = datetime.today() - timedelta(days=1)
  date= "%s-%s-%s" % (d.year,d.month,d.day)
  
  # date="2016-08-25"
  
  #date='2016-07-11'
  FitbitURL = "https://api.fitbit.com/1/user/-/profile.json"
  FitbitActivityURL = "https://api.fitbit.com/1/user/-/activities/date/"+date+".json"
  FitbitSleepURL = "https://api.fitbit.com/1/user/-/sleep/date/"+date+".json"
  

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
        if type=="fitness":
          self.add_events_fitness()
        elif type=="sleep":
          self.make_score_sleep()
        else:
          print "NoT FITNESS OR SLEEP?"

        #self.get_user_data(username,type)#NEW MIGHT BREAK SOMETHING IDK
      else:
       print self.ErrorInAPI

  def make_score_fitness(self,username):
    type="fitness"
    result=self.get_user_data(username,type)
    steps= result['summary']['steps']
    print steps
    if steps<=500:
      return None

    highly_active_min=result['summary']['veryActiveMinutes']
    fairly_active_min=result['summary']['fairlyActiveMinutes']
    lightly_active_min=result['summary']['lightlyActiveMinutes']
    sed_min=result['summary']['sedentaryMinutes']
    score = highly_active_min+(fairly_active_min*.5)+(lightly_active_min*.25)-(sed_min*.05)  #calculate their score
    score=score*10
    return score


  def add_events_fitness(self):

    db=SQLConnection()
    users=db.get_all_users()
    category="fitness"
    for username in users:
      if db.is_fitbit_setup(username):
        pet_name=db.find_pet_of_user(username)
        xp_change=self.make_score_fitness(username)
        type1=1
        wore_fitbit=True

        if xp_change==None:
          db.insert_events(username,category,"no_effect",0,"You did not sleep with your fitbit, so your score was uneffected",type1)
          wore_fitbit=False
          
        if wore_fitbit:
          db.change_xp(username,  xp_change)
          if xp_change<=-300:
            message="You have seriously harmed " +pet_name + " from neglagence yesterday. Make it up to " + pet_name +" by exercising today"
            pet_status="sick"
          elif xp_change<=100:
            message= pet_name+" is sad from the lack of exercise yesterday"
            pet_status="sick"
          elif xp_change<=300:
            message=pet_name+" got some exercise yesterday, but is still eager to play today "
            pet_status="happy"
          else:
            message=pet_name+" got plenty of exercise yesterday and as a result he is very happy! +" + str(xp_change) +" xp"
            pet_status="fit"
          print "added"
          current_xp=db.get_xp(username)
          db.insert_events(username,category,pet_status,xp_change,message,type1,current_xp)

  def make_score_sleep(self):
    type="sleep"
    db=SQLConnection()
    users=db.get_all_users()
    for username in users:
      if db.is_fitbit_setup(username):
        db.change_xp(username,result)
        pet_name=db.find_pet_of_user(username)
        result=self.get_user_data(username,type)
        print result
        result=result['summary']['totalTimeInBed']
        print result
        db=SQLConnection()
        if result==0:
          db.insert_events(username,"sleep","no_effect",0,"You did not sleep with your fitbit, so your score was uneffected","1")
        elif result<=400:
          db.insert_events(username,"sleep","sleepy",-250, pet_name +" is tired from the lack of sleep. -250 xp","1")
        elif result<=420:
          db.insert_events(username,"sleep","sleepy",-125, pet_name +" is tired from the lack of sleep. -250 xp","1")
        else:
          db.insert_events(username,"sleep","awake",250, pet_name +" feels great today! It must be from getting enough sleep. +250 xp","1")


#fitbit().add_events_fitness()

# fitbit().make_score_sleep()


#TODO FITBIT IT FAILS FIRST TIME WHEN IT NEEDS TO USE REFRESH TOKEN 
#fitbit().add_events_fitness()
