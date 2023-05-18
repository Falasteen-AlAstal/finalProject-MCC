package com.example.final_project_mcc

class ContactModel(var firstName:String?=null,var middleName:String?=null,var lastName:String?=null,
        var id: String?=null,var address:String?=null,var dateBirth:com.google.firebase.Timestamp? = null,
                   var email:String?=null,var password:String?=null,var phone:String?=null,
                   var role:String?=null
) {
}