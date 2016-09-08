import sys, ConfigParser, numpy
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
                self.con.commit
            return self.cur.fetchall()
    def close(self):
        self.cur.close()

    def check_or_add(self,user_name,pet_name):
        # print self.cur.execute("INSERT INTO users VALUES (7,%s,%s,'323',5000);" ,[username,"petty"])
        # self.cur.execute("INSERT INTO users VALUES (7,'ok','hu','323',5000);")
        # print self.cur.execute("SELECT * FROM `users`")
        # data = self.cur.fetchall ()
        # self.con.commit()
        # print data
        # return username
        
        try:
            self.query("INSERT INTO users VALUES (NULL,%s,%s,NULL,50000);" ,(user_name,pet_name))
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
             
    def check_transactions(self,user_id,event_id):
        self.query("INSERT INTO `event_list` (`user_id`, `event_id`, `timestamp`) VALUES (%s, %s, CURRENT_TIMESTAMP)",(user_id,event_id))
    
    def get_events(self):
        user_id=3
        timestamp="2016-09-01 00:00:00"
        new_events= self.query("SELECT event_id FROM `event_list` WHERE `user_id` = %s AND `timestamp` > %s",(user_id,timestamp))
        print (new_events)
        for items in new_events:
            event=self.query("SELECT message FROM `events` WHERE `id` = %s",[items["event_id"]]) #change message to * for full dict
           # print items["event_id"]
            print event
        
       # print type(result)
       # dog= self.cur.execute("SELECT access_id FROM `users` WHERE `user_name` LIKE %s",[username]) #works for now?
       # tup=self.cur.fetchall()#get it to work!
       # dict((y)for y in tup)
       # tup=str(tup)
       # print tup
       # d=json.loads(tup)
       # print d["access_id"]

    def change_xp(self,username,xp_change):
        xp=self.query("SELECT xp FROM `users` WHERE `user_name` = %s",[username])
        xp= xp[0]
        xp=xp['xp']+xp_change

        self.query("UPDATE `users` SET `xp` = %s WHERE `user_name`=%s",[xp,username])



SQLConnection().restraunt_xp('emc67',-5000) 
#dog=SQLConnection().check_or_add("ajs262","average_pup")
#print dog
#print SQLConnection().get_new_transactions()
#print SQLConnection().get_plaid_token("rwr21")