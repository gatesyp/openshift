# openshift

##setup

POST /setup/create_user


Parameters  
 
```
{  
  "create_acc": {  
    "user_name": "user",  
    "pet_name": "fluffychunks",  
    "pass_word": "hiimemerson"  
  }  
}
```


POST /setup/add_plaid  
Parameters  

```
{  
  "create_acc": {  
  "user_name": "user",
  "bank_name": "Chase",  
	"bank_user_name": "mybankusername",     
  "bank_pass_word": "hiimemerson"  
  }  
}
```


POST /setup/add_plaid  #not finished waiting on emc67/ig11



