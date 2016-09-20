
from passy import p
from plaid import Client
from plaid import errors as plaid_errors
from plaid.utils import json
from sql import SQLConnection

class plaid():
    account_type = 'chase'
    name=''
    password=''
    client=Client(client_id='57cccae6cfbf49f67b01fd5a', secret='2c13981884395fc691fda11148ed67')
    plaid_results=''
    def __init__(self):
        print "created plaid object"
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

    def use_token(self,username):
        obj=SQLConnection()
        token=obj.get_plaid_token(username)
        self.client = Client(client_id='57cccae6cfbf49f67b01fd5a', secret='2c13981884395fc691fda11148ed67', access_token=token)
        response=self.client.connect_get(token)
        self.plaid_results=response.content
        return response.content



    def create_user_plaid(self,username,account_type,bank_username,bank_password):
        try:
            response = self.client.connect(account_type, {
                        'username': bank_username,
                        'password': bank_password
                        })
            d=json.loads(response.content)
            access_token=d["access_token"]
            print access_token 
        except plaid_errors.PlaidError, e:
        	print e
        else:
            if response.status_code == 200:
                #print response.content
                db=SQLConnection()
                db.first_time_plaid(username,access_token)
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
    # db.close()
    def get_list_of_restraunt_transactions(self,username): #gets all of the restraunts you have viited from plaid in list form
        restraunt_ids_from_plaid=[]
        data=self.use_token(username)
        data=json.loads(data)
        for items in data['transactions']:
            add=False
            try:
                for things in items["category"]:
                    if things=="Fast Food" or things =="Restaurants":
                        add=True
            except:
                pass
            if add==True:
                restraunt_ids_from_plaid.append(items['_id'])

        return restraunt_ids_from_plaid

    def first_time_plaid_transactions(self,username):# add all plaid transactions that already occured into db
        obj=SQLConnection()
        id=obj.find_id_of_user(username)
        data=self.use_token(username)
        data=json.loads(data)
        for items in data['transactions']:
            add=False
            try:
                for things in items["category"]:
                    if things=="Fast Food" or things =="Restaurants":
                        add=True
            except:
                pass
            
            if add==True:
                obj.add_plaid_transactions_in_db(id,items["_id"])#adds the transaction id of any restraunts/fast_food 
            
    def check_plaid_event(self,username):#checks the database ids and plaid ids for differences
        plaid_list=self.get_list_of_restraunt_transactions(username)
        obj=SQLConnection()
        user_id=obj.find_id_of_user(username)
        database_list=obj.get_users_transactions(username)
        test=list(set(plaid_list)-set(database_list))
        if test!=[]: #if there is a difference aka update 
            data=self.plaid_results
            data=json.loads(data)
            for items in test:
                for things in data['transactions']:
                    if things['_id']==items:
                        fast_food=False
                        for classifications in things["category"]:
                            if classifications=="Fast Food":
                                fast_food=True
                        if fast_food==True:
                            obj.add_event_list(user_id,"1")
                            obj.add_plaid_transactions_in_db(user_id,things['_id'])
                            obj.change_xp(username,-500)
                        else:
                            obj.add_event_list(user_id,"2")
                            obj.add_plaid_transactions_in_db(user_id,things['_id'])
                            obj.change_xp(username,-250)
        return "done"



                    


# plaid().check_plaid_event("rwr21")
#plaid().check_plaid_event("rwr21")

    # print type(response)
    #             print type (response.content)
    #             d = json.loads(response.content)
    #             #print d
    #             print type(d)
    #            # print response.content
    #             for items in d['transactions']:

    #                 #TODO SQL DATABSE TO KEEP TRACK OF LVL FAST FOOD -200 restraunt -100

    #                # print items['date']
    #                 try:
    #                     for things in items['category']:
    #                         fast_food=False
    #                         if things=="Fast Food": #finds if user bought fast food
    #                             fast_food=True
    #                            #print items['name']
    #                     if fast_food==False:
    #                          for things in items['category']:
    #                             if things=="Restaurants":
    #                                 print things
    #                                 print items['name']#finds name of restraunt that is not fast food

    #                 except:
    #                     pass
    #                 #print items
    #             #print type (response.content.)
    #             # User connected
    #             data = response.json()
    #             
#rint SQLConnection().get_users_transactions("rwr21")
#plaid().first_time_plaid_transactions("rwr21")

#print plaid().use_token("rwr21")
#plaid().create_user("rwr21","chase","rratcliffe57",p)
#plaid().check_plaid_event("rwr21")