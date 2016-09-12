import base64
import urllib2
import urllib
import sys
import json
import os
from sql import SQLConnection

class fitbit():
  
  #This is the Fitbit URL to use for the API call
  FitbitURL = "https://api.fitbit.com/1/user/-/profile.json"
  FitbitActivityURL = "https://api.fitbit.com/1/user/-/activities/date/2016-08-25.json"
  FitbitSleepURL = "https://api.fitbit.com/1/user/-/sleep/date/2014-07-11.json"

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
  def get_user_data(self,username):

    AccessToken = ""
    RefreshToken = ""

    print "Fitbit API Test Code"

    #Get the config
    AccessToken, RefreshToken = self.GetConfig(username)

    #Make the API call
    APICallOK, APIResponse = self.MakeAPICall(self.FitbitActivityURL, AccessToken, RefreshToken,username)

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

  def make_score(self,username):
    result=self.get_user_data(username)
    steps= result['summary']['steps']
    highly_active_min=result['summary']['veryActiveMinutes']
    fairly_active_min=result['summary']['fairlyActiveMinutes']
    lightly_active_min=result['summary']['lightlyActiveMinutes']
    sed_min=result['summary']['sedentaryMinutes']
    score = highly_active_min+(fairly_active_min*.5)+(lightly_active_min*.25)-(sed_min*.05)
    print score*10
    

fitbit().make_score("rwr21")