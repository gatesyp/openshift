# from plaid import Client
# from plaid import errors as plaid_errors
#
#
# Client.config({
#     'url': 'https://tartan.plaid.com'
# })
#
# client = Client(client_id='57c6e5f766710877408d0a4b', secret='349711490014d9fac30f99cc7bd1e0')
#
# response = client.connect('chase', {
#         'username': 'rratcliffe57',
#         'password': p
# })
#
#
#
# response = client.auth('chase', {
#         'username': 'rratcliffe57',
#         'password': p
# })
# print response
#
#
# # from plaid import Client
# # from plaid import errors as plaid_errors
# # from plaid.utils import json
# #
# #
# # client = Client(client_id='57c6e5f766710877408d0a4b', secret='349711490014d9fac30f99cc7bd1e0')
# # institutions = client.institutions().json()
# # categories = client.categories().json()
#
#
# accounts = client.auth_get().json()
# print accounts
#
# # client = Client(client_id='57c6e5f766710877408d0a4b', secret='349711490014d9fac30f99cc7bd1e0', access_token='usertoken')
# # response = client.balance()
# # print response

# from plaid import Client
# from plaid import errors as plaid_errors
# from plaid.utils import json


# client = Client(client_id='7c6e5f766710877408d0a4b', secret='349711490014d9fac30f99cc7bd1e0')
account_type = 'chase'

# try:
#     response = client.connect(account_type, {
#      'username': 'rratcliffe57',
#      'password': p
#     })
#     print "try"
# except plaid_errors.PlaidError:
#      print "wow"
# else:
#     print "hello"
#     connect_data = response.json()

from passy import p
from plaid import Client
from plaid import errors as plaid_errors
from plaid.utils import json

Client.config({
    'url': 'https://tartan.plaid.com'
})

def answer_mfa(data):
	print "answer_mfa"
	if data['type'] == 'questions':
        # Ask your user for the answer to the question[s].
        # Although questions is a list, there is only ever a
        # single element in this list, at present
		return answer_question([q['question'] for q in data['mfa']])
	elif data['type'] == 'list':
		return answer_list(data['mfa'])
	elif data['type'] == 'selection':
		return answer_selections(data['mfa'])
	else:
		raise Exception('Unknown mfa type from Plaid')

def answer_question(questions):
	print "answer_question"
    # We have magically inferred the answer
    # so we respond immediately
    # In the real world, we would present questions[0]
    # to our user and submit their response
	answer = 'dogs'
	return client.connect_step(account_type, answer)


def answer_list(devices):
    print "answer_list"
    print devices[1]
	# You should specify the device to which the passcode is sent.
	# The available devices are present in the devices list
    dog= client.connect_step('chase', None, options={
	    'send_method': {'mask': 'xxx-xxx-9793'}
        })
    answer = raw_input("Please enter something: ")
    cat=client.connect_step(account_type, answer)
    print cat
    print cat.content

    # print dog
    # print type(dog)
    # new =dog.content
    # print new
    # print type(new)
    # token=new.access_token
    # print token
    return dog


def answer_selections(selections):
	print "answer_selections"
	# We have magically inferred the answers
	# so we respond immediately
	# In the real world, we would present the selection
	# questions and choices to our user and submit their responses
	# in a JSON-encoded array with answers provided
	# in the same order as the given questions
	answer = json.dumps(['Yes', 'No'])
	return client.connect_step(account_type, answer)


client = Client(client_id='57c6e5f766710877408d0a4b', secret='349711490014d9fac30f99cc7bd1e0')
account_type = 'chase'

try:
    response = client.connect(account_type, {
     'username': 'rratcliffe57',
     'password': p
    })
except plaid_errors.PlaidError, e:
	print "hi"
	print e
else:
    if response.status_code == 200:
    	print "his"
        #print response.content
        print type(response)
        print type (response.content)
        d = json.loads(response.content)
        #print d
        print type(d)
        for items in d['name']:
            print "hi"
        #print type (response.content.)
        # User connected
        data = response.json()
    elif response.status_code == 201:
    	print "yo"
        # MFA required
        try:
            mfa_response = answer_mfa(response.json())
        except plaid_errors.PlaidError, e:
       		print e
        else:
        	print "@@@@@@@@@@@@@@@@@@"
            # check for 200 vs 201 responses
            # 201 indicates that additional MFA steps required
#my_answer = raw_input("Please enter something: ")
#print "you entered", my_answer
# response = client.connect_step('chase', {
#     'username': 'rratcliffe57',
#     'password': p
# })
# print response

# client = Client(client_id='57c6e5f766710877408d0a4b', secret='349711490014d9fac30f99cc7bd1e0', access_token = token)
#
# response = client.balance()
# print response.content
# print response
# mfa_response = client.auth_step('chase', my_answer)
# print mfa_response
