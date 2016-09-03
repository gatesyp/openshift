import os
import MySQLdb
from datetime import datetime
from flask import Flask, request, flash, url_for, redirect, \
     render_template, abort, send_from_directory

db=MySQLdb.connect(
		host="localhost",
		user="root",
		passwd="",
		db="openshift")
# you must create a Cursor object. It will let
#  you execute all the queries you need
cur = db.cursor()

# Use all the SQL you like
#cur.execute("SELECT * FROM users")



app = Flask(__name__)
app.config.from_pyfile('flaskapp.cfg')

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/<path:resource>')
def serveStaticResource(resource):
    return send_from_directory('static/', resource)

@app.route("/test")
def test():
    return "<strong>It's Alive!</strong>"

@app.route("/add/<username>")
def add(username):

	cur.execute("INSERT INTO users VALUES (2, %s,5000);" ,[username])
	
	# cur.execute("select 1 from users where Username = %s;" , "hello")
	return username






if __name__ == '__main__':
    app.run()
