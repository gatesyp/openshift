import sys, ConfigParser
import MySQLdb as mdb
from plaid.utils import json
import math

class SQLConnection:
    """Used to connect to a SQL database and send queries to it"""
    config_file = 'db.cfg'
    section_name = 'Database Details'

    _db_name = ''
    _hostname = ''
    _ip_address = ''
    _username = ''
    _password = ''

    def __init__(self):
        config = ConfigParser.RawConfigParser()
        config.read(self.config_file)
        print "making"

        try:
            _db_name = config.get(self.section_name, 'db_name')
            _hostname = config.get(self.section_name, 'hostname')
            _ip_address = config.get(self.section_name, 'ip_address')
            _user = config.get(self.section_name, 'user')
            _password = config.get(self.section_name, 'password')
        except ConfigParser.NoOptionError as e:
            print ('one of the options in the config file has no value\n{0}: ' +
                '{1}').format(e.errno, e.strerror)
            sys.exit()


        self.con = mdb.connect(_hostname, _user, _password, _db_name)
        self.con.autocommit(False)
        self.con.ping(True)

        self.cur = self.con.cursor(mdb.cursors.DictCursor)

    ##############START CALLED ONLY IN SQL FILE###############
    def query(self, sql_query, values=None):
            """
            take in 1 or more query strings and perform a transaction
            @param sql_query: either a single string or an array of strings
                representing individual queries
            @param values: either a single json object or an array of json objects
                representing quoted values to insert into the relative query
                (values and sql_query indexes must line up)
            """
            #  TODO check sql_query and values to see if they are lists
            #  if sql_query is a string
            if isinstance(sql_query, basestring):
                self.cur.execute(sql_query, values)
                self.con.commit()
            #  otherwise sql_query should be a list of strings
            else:
                #  execute each query with relative values
                for query, sub_values in zip(sql_query, values):
                    self.cur.execute(query, sub_values)
                #  commit all these queries
                self.con.commit()
            return self.cur.fetchall()
    def close(self):
        self.cur.close()

    def add_event_list(self,user_id,event_id):
        self.query("INSERT INTO `event_list` (`id`,`user_id`, `event_id`, `timestamp`) VALUES (NULL,%s, %s, CURRENT_TIMESTAMP)",(user_id,event_id))
   

    ###########END CALLED ONLY IN SQL FILE###########



    #@@@@@@@@@@@@ START GENERAL METHODS @@@@@@@@@@@@@@#


    def hash_password(password):
         # uuid is used to generate a random number
        salt = uuid.uuid4().hex
        return hashlib.sha256(salt.encode() + password.encode()).hexdigest() + ':' + salt
    
    def check_password(hashed_password, user_password):
        password, salt = hashed_password.split(':')
        return password == hashlib.sha256(salt.encode() + user_password.encode()).hexdigest()

    #def hash_pass():
        







    def check_or_add(self,user_name,pet_name,password): # tries to create an account, if one does not exist it creates it. 
        try:
            password=self.hash_password(password)
            self.query("INSERT INTO users VALUES (NULL,%s,%s,NULL,NULL,NULL,5000,NULL,NULL,CURRENT_TIMESTAMP,%s);" ,(user_name,pet_name,password))
            print "Thank you for making an account!"
            return "1"
        except mdb.IntegrityError, e:
            print "Welcome Back!"

    def find_id_of_user(self, username):
        return self.query("SELECT id FROM `users` WHERE `user_name` LIKE %s",[username])[0]['id']# users id from id table as string
        
    def find_pet_of_user(self, username):
        return self.query("SELECT pet_name FROM `users` WHERE `user_name` LIKE %s",[username])[0]['pet_name'] #pet string id from users table

    def get_xp(self,username): #returns xp as int
        return self.query("SELECT xp FROM `users` WHERE `user_name` = %s",[username])[0]['xp']

    def change_xp(self,username,xp_change):#call this function to change user xp
        xp=self.query("SELECT xp FROM `users` WHERE `user_name` = %s",[username])
        xp= xp[0]
        xp=xp['xp']+xp_change
        self.query("UPDATE `users` SET `xp` = %s WHERE `user_name`=%s",[xp,username])

    def get_lvl(self,username):
        xp=self.get_xp(username)
        if xp<=5000:
            lvl=1
            print lvl
        else:
            lvl=math.floor((xp-5000)/(250))
            print lvl 
        

    def add_friend(self,username,friend_username):
        try:
            user_id=self.find_id_of_user(username)
            friend_id=self.find_id_of_user(friend_username)
            self.query("INSERT INTO `friends` (`user_id`, `friend_id`) VALUES ( %s, %s)",(user_id,friend_id))
            return "added friend"
        except mdb.IntegrityError, e: 
            return "you have already added this friend"
        except IndexError,e:
            return "One of the names that you entered does not exist"

    def get_friends(self,username): #RETURNS JSON key='user_name'  user_name:friend_name
        user_id=self.find_id_of_user(username)
        friends=self.query("SELECT friend_id FROM `friends` WHERE `user_id` = %s",[user_id])
        list_of_friends=[]
        for items in friends:
            temp= self.query("SELECT user_name FROM `users` WHERE `id` = %s",[items['friend_id']])
            temp=temp[0]
            list_of_friends.append(temp)
        print list_of_friends
        return list_of_friends

    def get_all_users(self): #returns list of all users
        users=[]
        temp= self.query("SELECT user_name FROM `users`")
        for items in temp:
            users.append(items['user_name'])
        return users

    def daily_xp_difference(self): #call this function to find difference in xp and last_day xp
        users=self.get_all_users()
        for people in users:
            xp_diff_column=self.query("SELECT last_day_xp FROM `users` WHERE `user_name` = %s",[people])[0]['last_day_xp']
            if xp_diff_column!=None:
                current_xp=self.get_xp(people)
                xp_diff=current_xp-xp_diff_column
                self.query("UPDATE `users` SET `xp_diff` = %s WHERE `users`.`user_name` = %s",[xp_diff,people])
        return "done"  

    def insert_events(self,username,category,pet_status,xp_change,message,type):
        self.query("INSERT INTO `events` (`id`, `category`,`pet_status`,`xp_change`,`message`,`type`) VALUES ( NULL, %s,%s, %s,%s,%s)",(category,pet_status,xp_change,message,type))
        temp= self.query("SELECT id FROM events ORDER BY id DESC LIMIT 0, 1")
        temp=temp[0]
        trans_id= temp['id']
        user_id=self.find_id_of_user(username)
        self.add_event_list(user_id,trans_id)

    def set_last_day_xp_to_xp(self):
        users=self.get_all_users()
        for username in users:
            xp=self.get_xp(username)
            self.query("UPDATE `users` SET `last_day_xp` = %s WHERE `users`.`user_name` = %s",[xp,username])

    def get_events(self,username): #changes timestamp on user, gets all new events in return_array and returns them (actually a list)
        return_array=[]
        user_id=self.find_id_of_user(username)
        timestamp=self.query("SELECT time FROM `users` WHERE `user_name` LIKE %s",[username])
        timestamp=timestamp[0]
        timestamp=timestamp["time"]
        new_events= self.query("SELECT event_id FROM `event_list` WHERE `user_id` = %s AND `timestamp` > %s",(user_id,timestamp))
        self.query("UPDATE `users` SET `time` = CURRENT_TIMESTAMP WHERE `user_name`=%s",[username])
        if new_events!=():      
            for items in new_events:
                event=self.query("SELECT message FROM `events` WHERE `id` = %s",[items["event_id"]]) #change message to * for full dict
                event=event[0]
                return_array.append(event)
            print return_array
            return return_array
            
        else:
            print return_array
            return return_array
#@@@@@@@@@@@@ END GENERAL METHODS @@@@@@@@@@@@@@#

    
############## Methods Involving Plaid ###############

    def first_time_plaid(self,username,id):
        return self.query("UPDATE `users` SET `access_id` = %s WHERE `users`.`user_name` = %s",[id,username])
        
    def get_plaid_token(self,username):
        return  self.query("SELECT access_id FROM `users` WHERE `user_name` LIKE %s",[username])[0]['access_id']
        
    def add_plaid_transactions_in_db(self,user_id,transaction_id):
        self.query("INSERT INTO `transactions` (`id`, `user_id`, `transaction_id`) VALUES (NULL, %s, %s)",(user_id,transaction_id))
        return "done"
    
    def get_users_transactions(self,username):
        transaction_ids=[]
        id=self.find_id_of_user(username)
        result=self.query("SELECT transaction_id FROM `transactions` WHERE `user_id` LIKE %s",[id])
        for items in result:
            transaction_ids.append(items["transaction_id"])
        return transaction_ids
             
    
############## END Methods Involving Plaid ###############
    


#@@@@@@@@@@@@ START METHODS INVOLVING FITBIT @@@@@@@@@@@@@@#


    def is_fitbit_setup(self,username): #checks to see if user has a fitbit id- this is checked before trying to use the fitbit algorithms on the user
        fitbit_acc_token=self.query("SELECT fitbit_acc_token FROM `users` WHERE `user_name` = %s",[username])
        fitbit_acc_token=fitbit_acc_token[0]
        if fitbit_acc_token['fitbit_acc_token']==None:
            return False
        else:
            return True

    def get_fitbit_acc_token(self,username):
        acc_token=self.query("SELECT fitbit_acc_token FROM `users` WHERE `user_name` = %s",[username])
        acc_token=acc_token[0]
        return acc_token['fitbit_acc_token']

    def get_fitbit_ref_token(self,username):
        ref_token=self.query("SELECT fitbit_ref_token FROM `users` WHERE `user_name` = %s",[username])
        ref_token=ref_token[0]
        return ref_token['fitbit_ref_token']

    def update_fitbit_acc_token(self,username,acc,ref):
         self.query("UPDATE `users` SET `fitbit_acc_token` = %s WHERE `users`.`user_name` = %s",[acc,username])
         self.query("UPDATE `users` SET `fitbit_ref_token` = %s WHERE `users`.`user_name` = %s",[ref,username])

#@@@@@@@@@@@@ END METHODS INVOLVING FITBIT @@@@@@@@@@@@@@#


############## START METHODS INVOLING SOCIAL I ###############


    def update_score_si(self): #all of the SI logic to calc score, then add event and update users scores
        users=self.get_all_users()
        for people in users:
            friends=self.get_friends(people)
            num_of_friends=0
            total_xp_change=0
            for items in friends:
                num_of_friends+=1
                items=items['user_name']
                friend_xp_change=self.query("SELECT xp_diff FROM `users` WHERE `user_name` = %s",[items])[0]['xp_diff']
                if friend_xp_change!=None:
                    total_xp_change=friend_xp_change+total_xp_change
                else:
                    num_of_friends-=1
            if num_of_friends!=0:
                average_xp_change=total_xp_change/num_of_friends
                self.change_xp(people,average_xp_change)
                # self.set_last_day_xp_to_xp(people)
                pet_name=self.find_pet_of_user(people)
                message="As a result of your friends, " +pet_name+ " recieved "+ str(average_xp_change) + "xp!"
                print average_xp_change
                self.insert_events(people,"Social impact","tbd_SI",average_xp_change,message ,1)
        
                
############## END METHODS INVOLING SOCIAL I ###############
    


SQLConnection().get_lvl("rwr21")
#SQLConnection().insert_events("rwr21","t","t",1,"t",1)






     


#