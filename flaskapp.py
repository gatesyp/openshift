import os
import MySQLdb
from datetime import datetime
from flask import Flask, request, flash, url_for, redirect, \
     render_template, abort, send_from_directory
from engine.sql import SQLConnection
from engine.plaid_test import plaid
from engine.fitbit import fitbit
import schedule
import time
from threading import Thread

def MyThread1():
	def call_fitbit_last_day():
		fb=fitbit()
		fb.add_events_fitness('rwr21')
	    

	
	schedule.every().day.at("10:00").do(call_fitbit_last_day)
	while 1:
		 schedule.run_pending()
		 time.sleep(1)
t1=Thread(target=MyThread1,args=[])
t1.start()

def MyThread2():
	def job1():
	    print("I'm working...sdasdasdsd")

	schedule.every(1).minutes.do(job1)
	schedule.every().hour.do(job1)
	schedule.every().day.at("10:30").do(job1)
	while 1:
		 schedule.run_pending()
		 time.sleep(1)
t2=Thread(target=MyThread2,args=[])
t2.start()

app = Flask(__name__)
app.config.from_pyfile('flaskapp.cfg')

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/<path:resource>')
def serveStaticResource(resource):
    return send_from_directory('static/', resource)

@app.route("/api/test")
def test():
    return "<strong>It's Alive!</strong>"

@app.route("/api/post")
def other():
    return "<strong>ROFL</strong>"

@app.route("/add_plaid/<username>") #must make sure this works with access_id
def add_plaid(username):
	#db=SQLConnection()
	return 0232
	p=plaid()
	p.create_user_plaid("emc67","chase","rratcliffe57","***REMOVED***")
	p.first_time_plaid_transactions("emc67")
	return username

@app.route("/get_new_events")
def get_new_events():
	db=SQLConnection()
	p=plaid()
	p.check_plaid_event("emc67")
	events= db.get_events("emc67")
	# for items in test:
	# 	print items['message']
	return events

@app.route("/add_friend")
def add_friend():
	SQLConnection().add_friend("rwr21","emc67")

@app.route("/get_friends")
def get_friends():
	db=SQLConnection()
	db.get_friends("rwr21")





if __name__ == '__main__':
    app.run()
