package com.technology.circles.apps.omanmade.ui_general_method;

import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Picasso;
import com.technology.circles.apps.omanmade.tags.Tags;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class UI_General_Method {

    @BindingAdapter("error")
    public static void setErrorUi(View view, String error)
    {
        if (view instanceof EditText)
        {
            EditText editText = (EditText) view;
            editText.setError(error);

        }else if (view instanceof TextView)
        {
            TextView textView = (TextView) view;
            textView.setError(error);

        }
    }

    @BindingAdapter("avatarUser")
    public static void avatarUser(View view,String endPoint)
    {
        if (view instanceof CircleImageView)
        {
            CircleImageView imageView = (CircleImageView) view;

            if (endPoint!=null)
            {

                //Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_AVATAR+endPoint)).placeholder(R.drawable.ic_user).fit().into(imageView);
            }else
            {
               // Picasso.with(imageView.getContext()).load(R.drawable.ic_user).fit().into(imageView);

            }
        }else if (view instanceof ImageView)
        {
            ImageView imageView = (ImageView) view;

            if (endPoint!=null)
            {

                Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_AVATAR+endPoint)).fit().into(imageView);
            }else
                {
                    //Picasso.with(imageView.getContext()).load(R.drawable.ic_user).fit().into(imageView);

                }
        }

    }




    @BindingAdapter({"date"})
    public static void displayTime(TextView textView,long time)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
        String sTime = dateFormat.format(new Date(time*1000));
        textView.setText(sTime);
    }










}
