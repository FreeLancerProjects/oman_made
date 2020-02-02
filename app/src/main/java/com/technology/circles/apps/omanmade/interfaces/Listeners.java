package com.technology.circles.apps.omanmade.interfaces;


import com.technology.circles.apps.omanmade.models.ContactUsModel;

public interface Listeners {


    interface BackListener
    {
        void back();
    }


    interface ContactListener
    {
        void  sendContact(ContactUsModel contactUsModel);
    }




    



}
