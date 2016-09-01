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
#         'password': '***REMOVED***'
# })
#
#
#
# response = client.auth('chase', {
#         'username': 'rratcliffe57',
#         'password': '***REMOVED***'
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

from plaid import Client
from plaid import errors as plaid_errors
from plaid.utils import json


client = Client(client_id='7c6e5f766710877408d0a4b', secret='349711490014d9fac30f99cc7bd1e0')
account_type = 'chase'

try:
    response = client.connect(account_type, {
     'username': 'rratcliffe57',
     'password': '***REMOVED***'
    })
    print "try"
except plaid_errors.PlaidError:
     print "wow"
else:
    print "hello"
    connect_data = response.json()
