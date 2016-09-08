from flask import Flask
from flask import request
app = Flask(__name__)

@app.route('/')
def hello_world():
    return "Hello world!"

@app.route('/api/login', methods=['GET', 'POST'])
def login_in():
    if request.method == 'POST':
        #I need to get the information from the request that was sent
        show = "Post Request: login"
    else:
        show = "Login Information Placeholder"
    return show

@app.route('/api/register', methods=['GET', 'POST'])
def register():
    if request.method == 'POST':
        #I need to get the information from the request that was sent
        show = "Post Request: register"
    else:
        show = "Register Information Placeholder"
    return show

@app.route('/api/invite', methods=['GET', 'POST'])
def invite():
    if request.method == 'POST':
        #I need to get the information from the request that was sent
        show = "Post Request: invite"
    else:
        show = "Invite Information Placeholder"
    return show

@app.route('/api/setup', methods=['GET', 'POST'])
def setup():
    if request.method == 'POST':
        #I need to get the information from the request that was sent
        show = "Post Request: setup"
    else:
        show = "Setup Information Placeholder"
    return show

@app.route('/api/get_updates', methods=['GET', 'POST'])
def get_updates():
    if request.method == 'POST':
        #I need to get the information from the request that was sent
        show = "Post Request: get_updates"
    else:
        show = "get_updates Information Placeholder"
    return show

@app.route('/api/<donger>', methods=['GET', 'POST'])
def dongers(donger):
    donger = 'emerson'
    return'Donger %s' % donger
