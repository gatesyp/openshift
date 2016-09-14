import sys, ConfigParser
import MySQLdb as mdb
from plaid.utils import json

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

    def check_or_add(self,user_name,pet_name): 
        try:
            self.query("INSERT INTO users VALUES (NULL,%s,%s,NULL,NULL,NULL,50000);" ,(user_name,pet_name))
            print "Thank you for making an account!"
            return "1"
        except mdb.IntegrityError, e:
            print "Welcome Back!"

    def get_new_transactions(self):
        pass
    def first_time_plaid(self,username,id):
        self.query("UPDATE `users` SET `access_id` = %s WHERE `users`.`user_name` = %s",[id,username])
        return "done"

    def get_plaid_token(self,username):
        #result=self.query("SELECT access_id FROM `users` WHERE `user_name` LIKE 'rwr21'")
        result= self.query("SELECT access_id FROM `users` WHERE `user_name` LIKE %s",[username])
        result= result[0]
        return (result["access_id"])
        #return dict(result)

    def find_id_of_user(self, username):
        result=self.query("SELECT id FROM `users` WHERE `user_name` LIKE %s",[username])
        result=result[0]
        return result["id"] #users id from users table 

    def find_pet_of_user(self, username):
        result=self.query("SELECT pet_name FROM `users` WHERE `user_name` LIKE %s",[username])
        result=result[0]
        return result["pet_name"] #users id from users table 


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
             
    def add_event_list(self,user_id,event_id):
        self.query("INSERT INTO `event_list` (`id`,`user_id`, `event_id`, `timestamp`,`time_of_orig_event`) VALUES (NULL,%s, %s, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)",(user_id,event_id))
    
    def get_events(self,username):
        return_array=[]
        user_id=self.find_id_of_user(username)
        #timestamp="2016-09-01 00:00:00"
        timestamp=self.query("SELECT time FROM `users` WHERE `user_name` LIKE %s",[username])
        timestamp=timestamp[0]
        #print timestamp
        timestamp=timestamp["time"]
        new_events= self.query("SELECT event_id FROM `event_list` WHERE `user_id` = %s AND `timestamp` > %s",(user_id,timestamp))
        self.query("UPDATE `users` SET `time` = CURRENT_TIMESTAMP WHERE `user_name`=%s",[username])
        #print (new_events)
        if new_events!=():      
            for items in new_events:
                event=self.query("SELECT message FROM `events` WHERE `id` = %s",[items["event_id"]]) #change message to * for full dict
                event=event[0]
                return_array.append(event)
            return return_array
        else:
            
            return return_array

    def change_xp(self,username,xp_change):
        xp=self.query("SELECT xp FROM `users` WHERE `user_name` = %s",[username])
        xp= xp[0]
        xp=xp['xp']+xp_change
        self.query("UPDATE `users` SET `xp` = %s WHERE `user_name`=%s",[xp,username])

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


    def add_friend(self,username,friend_username):
        user_id=self.find_id_of_user(username)
        friend_id=self.find_id_of_user(friend_username)
        self.query("INSERT INTO `friends` (`user_id`, `friend_id`) VALUES ( %s, %s)",(user_id,friend_id))
        

    def get_friends(self,username):
        user_id=self.find_id_of_user(username)
        friends=self.query("SELECT friend_id FROM `friends` WHERE `user_id` = %s",[user_id])
        list_of_friends=[]
        for items in friends:
            temp= self.query("SELECT user_name FROM `users` WHERE `id` = %s",[items['friend_id']])
            temp=temp[0]
            list_of_friends.append(temp)
        print list_of_friends
        return list_of_friends

    def insert_fitbit_events(self,username,category,pet_status,xp_change,message,type):
        self.query("INSERT INTO `events` (`id`, `category`,`pet_status`,`xp_change`,`message`,`type`) VALUES ( NULL, %s,%s, %s,%s,%s)",(category,pet_status,xp_change,message,type))
        temp= self.query("SELECT id FROM events ORDER BY id DESC LIMIT 0, 1")
        temp=temp[0]
        trans_id= temp['id']
        user_id=self.find_id_of_user(username)
        self.add_event_list(user_id,trans_id)

    def get_all_users(self):
        users=[]
        temp= self.query("SELECT user_name FROM `users`")
        for items in temp:
            users.append(items['user_name'])

        return users


SQLConnection().get_all_users()


     


#SQLConnection().add_friend("rwr21","ajs262")

#SQLConnection().get_friends("rwr21")
# print SQLConnection().get_fitbit_ref_token("rwr21")
# d=SQLConnection()
#dog=SQLConnection().check_or_add("ajs262","average_pup")
#print dog
#print SQLConnection().get_new_transactions()
#print SQLConnection().get_plaid_token("rwr21")