Starter code for Summer 2024 version of group project.

# Progress Report

Person: Boshu Lei, Tim Song, Ruxi Xu

---

## 1.1 DataManager Test Case (Boshu Lei)

```
DataManager_attemptLogin_Test.java
DataManager_createFund_Test.java
DataManager_getContributorName_Test.java
```

## 1.2 Bug Fix

Check fund id to be valid.

## 1.3 **main menu input handling** (Tim Song)

Add quit goodbye; 

Add while loops to parse input string until the program get a valid option

## 1.4 D**isplay total donations for fund** (Ruxi Xu)

modified displayFund function in UserInterface to show total amount of donation and percentage of target

## 1.8 **createFund input error handling** (Tim Song)

Add while loops to detect blank names

Add while loops to detect blank descriptions

Add while loops to parse target number, until the program gets a positive target number.

## 1.9 Login Error Handle (Boshu Lei)

throw IllegalStateException when response is null; catch this error in the main loop and display `Error in communicating with server` in Console, otherwise display `Login Fail` if the response status if fail. 

## 1.10 Date formatting (Ruxi Xu)

Format the date string in `UserInterface` and set the donation.

----

# Phase 2

## 2.1 Cache

In `DataManager`, a hash map is used for caching. 

```Java
private Map<String, String> contributorCache = new HashMap<>();
```

Each time when calling `getContributorName` method, the program will first check whether the Cache contains the id. If it contains, then the data in cache will be returned. Otherwise, the programm will query the server for the name. 

## 2.2 (Ruxi Xu)

Added `DataManagerRobustnessTests.java` in Organization App. All thest are passed.
UI is updated to display meaningful error message.

## 2.3 Organization App aggregate donations by contributor (Tim Song)

I create a new class `AggregateDonation` which contains the aggregated donation information for each fund.
It is created during the creation of that fund in the memory.

## 2.7 Organization App delete fund
I append a while loop in the end of display fund function to ask the user whether to delete this fund. 
I add a new function in the DataManager class, and also delete the fund info in the memory object.
I add unit test and mock objects to test the new function.

## 2.8 (Ruxi Xu)

Added logout function in UI classes; login again feature is also added to let user log back in after logout.

## 2.10 Encryptation (Boshu Lei)

Prepare the `public_key.pem` under `\org` and `\admin` folder. Prepare the `private_key.pem` under `\admin` folder. 

When launching javascript, please use the following command.

```
 node --security-revert=CVE-2023-46809 .\api.js
 node .\admin.js
```

# phase 3

## 3.1 New User Registration (Boshu Lei)

Add a new function `/createOrg` in the `api.js` to crreate new organization after receiving requests from `org` app. If the login has already exists, it will send failure signal back to `org` app. In the `org` app, it will first prompt the user to choose from Login or Register. If the user chooses Register, then they will type the login, password, description and name of the new organization. Any illegal inputs will be detected and the user should re-type them again. 

## 3.2 Change Password (Boshu Lei)

After sucessfully login in, the user can type `m` to change the password. When changing the password, they are asked to input the current password and type the new password twice for confimation. A new function `/updateOrg` is added in the `api.js` to process the updating request from the client. 

## 3.3 Edit account information (Yunzhou Song)
Add a new function in the UserInterface class to modify the organization information. 
Prompt the user to enter their choice and check whether the input is valid. The input is cached in local temporary memory.
Reuse the `dataManager.updatePassword()` function to modify the organization info. Input the new name and description, but keep other information as before.
If the function return true, then modify the organization info in the object. Otherwise report error and abort edit.

## 3.4 Make Donation (Ruxi (Sophia) Xu)

The user can choose to make donation in the `org` app after viewing the fund in the organization. The `org` app will ask the user to input the fund id, contributor id and money. A new function `/makeDonation` is created in the `api.js` file to process request from `org` app. 

