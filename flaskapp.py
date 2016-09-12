import os
import MySQLdb
from datetime import datetime
from flask import Flask, request, flash, url_for, redirect, \
     render_template, abort, send_from_directory
from engine.sql import SQLConnection
from engine.plaid_test import plaid
from engine.fitbit import fitbit


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
def test():
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

	




if __name__ == '__main__':
    app.run()
