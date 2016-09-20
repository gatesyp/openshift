import base64
import urllib2
import urllib
import sys
import json
import os
import uuid
import hashlib
from sql import SQLConnection

class social():
	"""docstring for social"""
	def __init__(self):
		"print created social object"

	def calculate_social(self):
		db=SQLConnection()
		db.daily_xp_difference()
		db.update_score_si()
		db.set_last_day_xp_to_xp()

#social().calculate_social()
		

# def hash_password(password):
#     # uuid is used to generate a random number
#     salt = uuid.uuid4().hex
#     return hashlib.sha256(salt.encode() + password.encode()).hexdigest() + ':' + salt
    
# def check_password(hashed_password, user_password):
#     password, salt = hashed_password.split(':')
#     return password == hashlib.sha256(salt.encode() + user_password.encode()).hexdigest()
 
# # new_pass = raw_input('Please enter a password: ')
# hashed_password = hash_password(new_pass)
# print('The string to store in the db is: ' + hashed_password)
# old_pass = raw_input('Now please enter the password again to check: ')
# if check_password(hashed_password, old_pass):
#     print('You entered the right password')
# else:
#     print('I am sorry but the password does not match')