package com.technology.circles.apps.omanmade.ui_general_method;

import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.makeramen.roundedimageview.RoundedImageView;
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

    @BindingAdapter("sliderImage")
    public static void sliderImage(View view,String endPoint)
    {
        if (view instanceof CircleImageView)
        {
            CircleImageView imageView = (CircleImageView) view;

            if (endPoint!=null)
            {

                Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_URL_SLIDER+endPoint)).fit().into(imageView);
            }
        }else if (view instanceof RoundedImageView)
        {
            RoundedImageView imageView = (RoundedImageView) view;

            if (endPoint!=null)
            {

                Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_URL_SLIDER+endPoint)).fit().into(imageView);
            }
        }else if (view instanceof ImageView)
        {
            ImageView imageView = (ImageView) view;

            if (endPoint!=null)
            {

                Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_URL_SLIDER+endPoint)).fit().into(imageView);
            }
        }

    }

    @BindingAdapter("sponsorImage")
    public static void sponsorImage(View view,String endPoint)
    {
        if (view instanceof CircleImageView)
        {
            CircleImageView imageView = (CircleImageView) view;

            if (endPoint!=null)
            {

                Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_URL_SPONSOR+endPoint)).into(imageView);
            }
        }else if (view instanceof RoundedImageView)
        {
            RoundedImageView imageView = (RoundedImageView) view;

            if (endPoint!=null)
            {

                Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_URL_SPONSOR+endPoint)).into(imageView);
            }
        }else if (view instanceof ImageView)
        {
            ImageView imageView = (ImageView) view;

            if (endPoint!=null)
            {

                Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_URL_SPONSOR+endPoint)).into(imageView);
            }
        }

    }

    @BindingAdapter("featuredCategoryImage")
    public static void featuredCategoryImage(View view,String endPoint)
    {
        if (view instanceof CircleImageView)
        {
            CircleImageView imageView = (CircleImageView) view;

            if (endPoint!=null)
            {

                Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_URL_FEATURED_CATEGORY+endPoint)).fit().into(imageView);
            }
        }else if (view instanceof RoundedImageView)
        {
            RoundedImageView imageView = (RoundedImageView) view;

            if (endPoint!=null)
            {

                Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_URL_FEATURED_CATEGORY+endPoint)).fit().into(imageView);
            }
        }else if (view instanceof ImageView)
        {
            ImageView imageView = (ImageView) view;

            if (endPoint!=null)
            {

                Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_URL_FEATURED_CATEGORY+endPoint)).fit().into(imageView);
            }
        }

    }

    @BindingAdapter("directoryImage")
    public static void directoryImage(View view,String endPoint)
    {
        if (view instanceof CircleImageView)
        {
            CircleImageView imageView = (CircleImageView) view;

            if (endPoint!=null)
            {

                Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_URL_DIRECTORY+endPoint)).fit().into(imageView);
            }
        }else if (view instanceof RoundedImageView)
        {
            RoundedImageView imageView = (RoundedImageView) view;

            if (endPoint!=null)
            {

                Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_URL_DIRECTORY+endPoint)).fit().into(imageView);
            }
        }else if (view instanceof ImageView)
        {
            ImageView imageView = (ImageView) view;

            if (endPoint!=null)
            {

                Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_URL_DIRECTORY+endPoint)).fit().into(imageView);
            }
        }

    }

    @BindingAdapter("industrialAreaImage")
    public static void industrialAreaImage(View view,String endPoint)
    {
        if (view instanceof CircleImageView)
        {
            CircleImageView imageView = (CircleImageView) view;

            if (endPoint!=null)
            {

                Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_URL_INDUSTRIAL_AREA +endPoint)).fit().into(imageView);
            }
        }else if (view instanceof RoundedImageView)
        {
            RoundedImageView imageView = (RoundedImageView) view;

            if (endPoint!=null)
            {

                Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_URL_INDUSTRIAL_AREA +endPoint)).fit().into(imageView);
            }
        }else if (view instanceof ImageView)
        {
            ImageView imageView = (ImageView) view;

            if (endPoint!=null)
            {

                Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_URL_INDUSTRIAL_AREA +endPoint)).fit().into(imageView);
            }
        }

    }

    @BindingAdapter("featureListingImage")
    public static void featureListingImage(View view,String endPoint)
    {
        if (view instanceof CircleImageView)
        {
            CircleImageView imageView = (CircleImageView) view;

            if (endPoint!=null)
            {

                Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_URL_FEATURE_LISTING +endPoint)).fit().into(imageView);
            }
        }else if (view instanceof RoundedImageView)
        {
            RoundedImageView imageView = (RoundedImageView) view;

            if (endPoint!=null)
            {

                Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_URL_FEATURE_LISTING +endPoint)).fit().into(imageView);
            }
        }else if (view instanceof ImageView)
        {
            ImageView imageView = (ImageView) view;

            if (endPoint!=null)
            {

                Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_URL_FEATURE_LISTING +endPoint)).fit().into(imageView);
            }
        }

    }

    @BindingAdapter("serviceImage")
    public static void serviceImage(View view,String endPoint)
    {
        if (view instanceof CircleImageView)
        {
            CircleImageView imageView = (CircleImageView) view;

            if (endPoint!=null)
            {

                Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_URL_SERVICE +endPoint)).fit().into(imageView);
            }
        }else if (view instanceof RoundedImageView)
        {
            RoundedImageView imageView = (RoundedImageView) view;

            if (endPoint!=null)
            {

                Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_URL_SERVICE +endPoint)).fit().into(imageView);
            }
        }else if (view instanceof ImageView)
        {
            ImageView imageView = (ImageView) view;

            if (endPoint!=null)
            {

                Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_URL_SERVICE +endPoint)).fit().into(imageView);
            }
        }

    }

    @BindingAdapter("settingImage")
    public static void settingImage(View view,String endPoint)
    {
        if (view instanceof CircleImageView)
        {
            CircleImageView imageView = (CircleImageView) view;

            if (endPoint!=null)
            {

                Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_URL_SETTING +endPoint)).fit().into(imageView);
            }
        }else if (view instanceof RoundedImageView)
        {
            RoundedImageView imageView = (RoundedImageView) view;

            if (endPoint!=null)
            {

                Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_URL_SETTING +endPoint)).fit().into(imageView);
            }
        }else if (view instanceof ImageView)
        {
            ImageView imageView = (ImageView) view;

            if (endPoint!=null)
            {

                Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_URL_SETTING +endPoint)).fit().into(imageView);
            }
        }

    }

    @BindingAdapter("mediaImage")
    public static void mediaImage(View view,String path)
    {
        if (view instanceof CircleImageView)
        {
            CircleImageView imageView = (CircleImageView) view;

            if (path!=null)
            {

                Picasso.with(imageView.getContext()).load(Uri.parse(path)).fit().into(imageView);
            }
        }else if (view instanceof RoundedImageView)
        {
            RoundedImageView imageView = (RoundedImageView) view;

            if (path!=null)
            {

                Picasso.with(imageView.getContext()).load(Uri.parse(path)).fit().into(imageView);
            }
        }else if (view instanceof ImageView)
        {
            ImageView imageView = (ImageView) view;

            if (path!=null)
            {

                Picasso.with(imageView.getContext()).load(Uri.parse(path)).fit().into(imageView);
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
