import os
import MySQLdb
from datetime import datetime
from flask import Flask, request, flash, url_for, redirect, \
     render_template, abort, send_from_directory
from engine.sql import SQLConnection
from engine.plaid_test import plaid
from engine.fitbit import fitbit
from engine.social import social
import schedule
import time
from threading import Thread
import json


#########ALL THREADS##############
def MyThread1():
	def call_fitbit_last_day():
		fb=fitbit()
		fb.add_events_fitness()
	    
	schedule.every().day.at("10:00").do(call_fitbit_last_day)
	while 1:
		 schedule.run_pending()
		 time.sleep(1)
t1=Thread(target=MyThread1,args=[])
t1.daemon = True
t1.start()





def MyThread2():
	def call_si():
	    si=social()
	    si.calculate_social()

	
	schedule.every().day.at("1:00").do(call_si)
	while 1:
		 schedule.run_pending()
		 time.sleep(1)
t2=Thread(target=MyThread2,args=[])
t2.daemon = True
t2.start()






#########END ALL THREADS##############



app = Flask(__name__)
app.config.from_pyfile('flaskapp.cfg')

@app.route('/')
def index():
    return render_template('index.html')



@app.route('/setup/create_user',methods=["POST"])
def create_user():
	try:
		db=SQLConnection()
		data=request.get_json(force=True)
		print data
		user_name=data['user_name']
		pet_name=data['pet_name']
		password=data['pass_word']
		print user_name
		print pet_name
		print password
		return db.check_or_add(user_name,pet_name,password)
		
	except:
		return "create_user_error"

@app.route('/setup/login',methods=["POST"])
def setup_login():
	db=SQLConnection()
	data=request.get_json(force=True)
	username=data["user_name"]
	password=data["pass_word"]
	return db.login(username,password)

@app.route("/setup/add_plaid",methods=["POST"]) #must make sure this works with access_id
def add_plaid(username):

	#db=SQLConnection()
	return 0232
	p=plaid()
	p.create_user_plaid("emc67","chase","rratcliffe57","roos_pass")
	p.first_time_plaid_transactions("emc67")
	return username


@app.route("/friends/add", methods=["POST"])
def add_friend():
	try:
		data=request.get_json(force=True)
		friend=data['friend_user_name']
		user=data['user_name']
		SQLConnection().add_friend(user,friend)
		return json.dumps(data)
	except:
		print "error add_friend"

@app.route("/friends/find",methods=["POST"])
def get_friends():
	try:
		temp={}
		db=SQLConnection()
		data=request.get_json(force=True)
		user=data['user_name']
		temp["friends"]=db.get_friends(user)
		return json.dumps(temp)
	except:
		return "error get_friends"

@app.route("/friends/find_advanced",methods=["POST"])
def get_friends_advanced():
	try:
		db=SQLConnection()
		data=request.get_json(force=True)
		user=data['user_name']
		temp=db.get_advanced_friend_data(user)
		return temp
	except:
		return "get_friends_advanced error"






@app.route("/events/plaid",methods=["POST"])
def events_plaid():
	try:
		p=plaid()	
		data=request.get_json(force=True)
		username=data['user_name']
		p.check_plaid_event(username)
	except:
		print "error events_plaid"

@app.route("/events/get_new",methods =["POST"])
def get_new_events():
	try:
		db=SQLConnection()
		p=plaid()
		data=request.get_json(force=True)
		user=data['user_name']
		p.check_plaid_event(user)
		events= db.get_events(user)
		# for items in test:
		# 	print items['message']
		return json.dumps(events)

	except:
		return "error get_new_events"

@app.route("/events/get_last_few",methods =["POST"])
def get_last_few():
	try:
		db=SQLConnection()
		data=request.get_json(force=True)
		user_name=data['user_name']
		num=int(data['num'])
		temp=db.get_last(user_name,num)
		return (temp)
	except:
		print "get_last_few error"




@app.route('/<path:resource>')
def serveStaticResource(resource):
    return send_from_directory('static/', resource)

@app.route("/api/test",methods =["POST","GET"])
def test():
	json_test= { "user_name": "e" }  
	return json.dumps(json_test)

@app.route("/api/post")
def other():
    return "<strong>ROFL</strong>"















if __name__ == '__main__':
    app.run(debug=False)
