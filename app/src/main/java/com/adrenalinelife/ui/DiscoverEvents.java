package com.adrenalinelife.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.adrenalinelife.R;
import com.adrenalinelife.custom.CustomFragment;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import io.fabric.sdk.android.Fabric;

public class DiscoverEvents extends CustomFragment {

    ImageButton yogaButton;
    ImageButton basketballButton;
    ImageButton soccerButton;
    ImageButton frisbeeButton;
    ImageButton golfButton;
    ImageButton tennisButton;
    ImageButton fishingButton;
    ImageButton kickballButton;
    ImageButton tablegamesButton;
    ImageButton watergamesButton;
    ImageButton baseballButton;
    ImageButton billiardsButton;
    ImageButton fitnessButton;
    ImageButton climbingButton;
    ImageButton ridingButton;
    ImageButton runningButton;
    ImageButton lacrosseButton;
    ImageButton footballButton;
    ImageButton campingButton;
    ImageButton bowlingButton;

    public Bundle mBundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View v = inflater.inflate(R.layout.discover_events, null);
        setHasOptionsMenu(false);

        /** Fabric Initializing **/
        Fabric.with(getActivity(), new Answers());
        Fabric.with(getActivity(), new Crashlytics());
        final Fabric fabric = new Fabric.Builder(getActivity())
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);


        //yoga
        yogaButton = (ImageButton) v.findViewById(R.id.b_yoga);
        yogaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {

                /** Fabric **/
                Answers.getInstance().logCustom(new CustomEvent("Categories_Discover")
                        .putCustomAttribute("Category", "Yoga"));

                mBundle = new Bundle();
                mBundle.putString("Filter", "yoga");

                //Launching a Fragment Code
                CategoryEvents catEvents = new CategoryEvents();
                catEvents.setArguments(mBundle);
                android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.content_frame, catEvents);
                transaction.addToBackStack("Yoga");
                transaction.commit();
            }
        });

        //ImageButton basketballButton;
        basketballButton = (ImageButton) v.findViewById(R.id.b_basketball);
        basketballButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {
                /** Fabric **/
                Answers.getInstance().logCustom(new CustomEvent("Categories_Discover")
                        .putCustomAttribute("Category", "Basketball"));
                mBundle = new Bundle();
                mBundle.putString("Filter", "basketball");

                CategoryEvents catEvents = new CategoryEvents();
                catEvents.setArguments(mBundle);
                android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.content_frame, catEvents);
                transaction.addToBackStack("Basketball");
                transaction.commit();
            }
        });

        //ImageButton soccerButton;
        soccerButton = (ImageButton) v.findViewById(R.id.b_soccer);
        soccerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {
                /** Fabric **/
                Answers.getInstance().logCustom(new CustomEvent("Categories_Discover")
                        .putCustomAttribute("Category", "Soccer"));
                mBundle = new Bundle();
                mBundle.putString("Filter", "soccer");
                CategoryEvents catEvents = new CategoryEvents();
                catEvents.setArguments(mBundle);
                android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.content_frame, catEvents);
                transaction.addToBackStack("Soccer");
                transaction.commit();
            }
        });


        //ImageButton golfButton;
        golfButton = (ImageButton) v.findViewById(R.id.b_golf);
        golfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {
                /** Fabric **/
                Answers.getInstance().logCustom(new CustomEvent("Categories_Discover")
                        .putCustomAttribute("Category", "Golf"));
                mBundle = new Bundle();
                mBundle.putString("Filter", "golf");
                CategoryEvents catEvents = new CategoryEvents();
                catEvents.setArguments(mBundle);
                android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.content_frame, catEvents);
                transaction.addToBackStack("Golf");
                transaction.commit();
            }
        });

        //ImageButton frisbeeButton;
        frisbeeButton = (ImageButton) v.findViewById(R.id.b_frisbee);
        frisbeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {
                /** Fabric **/
                Answers.getInstance().logCustom(new CustomEvent("Categories_Discover")
                        .putCustomAttribute("Category", "Frisbee"));
                mBundle = new Bundle();
                mBundle.putString("Filter", "frisbee");
                CategoryEvents catEvents = new CategoryEvents();
                catEvents.setArguments(mBundle);
                android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.content_frame, catEvents);
                transaction.addToBackStack("Frisbee");
                transaction.commit();
            }
        });

        //ImageButton tennisButton;
        tennisButton = (ImageButton) v.findViewById(R.id.b_tennis);
        tennisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {
                /** Fabric **/
                Answers.getInstance().logCustom(new CustomEvent("Categories_Discover")
                        .putCustomAttribute("Category", "Tennis"));
                mBundle = new Bundle();
                mBundle.putString("Filter", "tennis");
                CategoryEvents catEvents = new CategoryEvents();
                catEvents.setArguments(mBundle);
                android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.content_frame, catEvents);
                transaction.addToBackStack("Tennis");
                transaction.commit();
            }
        });

        //ImageButton fishingButton;
        fishingButton = (ImageButton) v.findViewById(R.id.b_fishing);
        fishingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {
                /** Fabric **/
                Answers.getInstance().logCustom(new CustomEvent("Categories_Discover")
                        .putCustomAttribute("Category", "Fishing"));
                mBundle = new Bundle();
                mBundle.putString("Filter", "fishing");
                CategoryEvents catEvents = new CategoryEvents();
                catEvents.setArguments(mBundle);
                android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.content_frame, catEvents);
                transaction.addToBackStack("Fishing");
                transaction.commit();
            }
        });


        //ImageButton baseballButton;
        baseballButton = (ImageButton) v.findViewById(R.id.b_baseball);
        baseballButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {
                /** Fabric **/
                Answers.getInstance().logCustom(new CustomEvent("Categories_Discover")
                        .putCustomAttribute("Category", "Baseball/Softball"));
                mBundle = new Bundle();
                mBundle.putString("Filter", "baseball");
                CategoryEvents catEvents = new CategoryEvents();
                catEvents.setArguments(mBundle);
                android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.content_frame, catEvents);
                transaction.addToBackStack("Baseball/Softball");
                transaction.commit();
            }
        });

        //ImageButton kickballButton;
        kickballButton = (ImageButton) v.findViewById(R.id.b_kickball);
        kickballButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {
                /** Fabric **/
                Answers.getInstance().logCustom(new CustomEvent("Categories_Discover")
                        .putCustomAttribute("Category", "Kickball"));
                mBundle = new Bundle();
                mBundle.putString("Filter", "kickball");
                CategoryEvents catEvents = new CategoryEvents();
                catEvents.setArguments(mBundle);
                android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.content_frame, catEvents);
                transaction.addToBackStack("Kickball");
                transaction.commit();
            }
        });

        //ImageButton tablegamesButton;
        tablegamesButton = (ImageButton) v.findViewById(R.id.b_tablegames);
        tablegamesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {
                /** Fabric **/
                Answers.getInstance().logCustom(new CustomEvent("Categories_Discover")
                        .putCustomAttribute("Category", "Table Games"));
                mBundle = new Bundle();
                mBundle.putString("Filter", "table");
                CategoryEvents catEvents = new CategoryEvents();
                catEvents.setArguments(mBundle);
                android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.content_frame, catEvents);
                transaction.addToBackStack("Table Games");
                transaction.commit();
            }
        });

        //ImageButton moreButton;
        billiardsButton = (ImageButton) v.findViewById(R.id.b_billiards);
        billiardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {
                /** Fabric **/
                Answers.getInstance().logCustom(new CustomEvent("Categories_Discover")
                        .putCustomAttribute("Category", "Billiards"));
                mBundle = new Bundle();
                mBundle.putString("Filter", "billiards");
                CategoryEvents catEvents = new CategoryEvents();
                catEvents.setArguments(mBundle);
                android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.content_frame, catEvents);
                transaction.addToBackStack("Billiards");
                transaction.commit();
            }
        });

        //ImageButton moreButton;
        fitnessButton = (ImageButton) v.findViewById(R.id.b_fitness);
        fitnessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {
                /** Fabric **/
                Answers.getInstance().logCustom(new CustomEvent("Categories_Discover")
                        .putCustomAttribute("Category", "Fitness"));
                mBundle = new Bundle();
                mBundle.putString("Filter", "fitness");
                CategoryEvents catEvents = new CategoryEvents();
                catEvents.setArguments(mBundle);
                android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.content_frame, catEvents);
                transaction.addToBackStack("Fitness");
                transaction.commit();
            }
        });

        //ImageButton moreButton;
        climbingButton = (ImageButton) v.findViewById(R.id.b_climbing);
        climbingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {
                /** Fabric **/
                Answers.getInstance().logCustom(new CustomEvent("Categories_Discover")
                        .putCustomAttribute("Category", "Climbing"));
                mBundle = new Bundle();
                mBundle.putString("Filter", "climbing");
                CategoryEvents catEvents = new CategoryEvents();
                catEvents.setArguments(mBundle);
                android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.content_frame, catEvents);
                transaction.addToBackStack("Climbing");
                transaction.commit();
            }
        });

        //ImageButton moreButton;
        lacrosseButton = (ImageButton) v.findViewById(R.id.b_lacrosse);
        lacrosseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {
                /** Fabric **/
                Answers.getInstance().logCustom(new CustomEvent("Categories_Discover")
                        .putCustomAttribute("Category", "Lacrosse"));
                mBundle = new Bundle();
                mBundle.putString("Filter", "lacrosse");
                CategoryEvents catEvents = new CategoryEvents();
                catEvents.setArguments(mBundle);
                android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.content_frame, catEvents);
                transaction.addToBackStack("Lacrosse");
                transaction.commit();
            }
        });

        //ImageButton moreButton;
        footballButton = (ImageButton) v.findViewById(R.id.b_football);
        footballButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {
                /** Fabric **/
                Answers.getInstance().logCustom(new CustomEvent("Categories_Discover")
                        .putCustomAttribute("Category", "Football"));
                mBundle = new Bundle();
                mBundle.putString("Filter", "football");
                CategoryEvents catEvents = new CategoryEvents();
                catEvents.setArguments(mBundle);
                android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.content_frame, catEvents);
                transaction.addToBackStack("Football");
                transaction.commit();
            }
        });

        //ImageButton moreButton;
        runningButton = (ImageButton) v.findViewById(R.id.b_running);
        runningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {
                /** Fabric **/
                Answers.getInstance().logCustom(new CustomEvent("Categories_Discover")
                        .putCustomAttribute("Category", "Running"));
                mBundle = new Bundle();
                mBundle.putString("Filter", "running");
                CategoryEvents catEvents = new CategoryEvents();
                catEvents.setArguments(mBundle);
                android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.content_frame, catEvents);
                transaction.addToBackStack("Running/Hiking");
                transaction.commit();
            }
        });

        //ImageButton moreButton;
        ridingButton = (ImageButton) v.findViewById(R.id.b_riding);
        ridingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {
                /** Fabric **/
                Answers.getInstance().logCustom(new CustomEvent("Categories_Discover")
                        .putCustomAttribute("Category", "Riding"));
                mBundle = new Bundle();
                mBundle.putString("Filter", "riding");
                CategoryEvents catEvents = new CategoryEvents();
                catEvents.setArguments(mBundle);
                android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.content_frame, catEvents);
                transaction.addToBackStack("Riding");
                transaction.commit();
            }
        });

        //ImageButton moreButton;
        watergamesButton = (ImageButton) v.findViewById(R.id.b_water);
        watergamesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {
                /** Fabric **/
                Answers.getInstance().logCustom(new CustomEvent("Categories_Discover")
                        .putCustomAttribute("Category", "Water Games"));
                mBundle = new Bundle();
                mBundle.putString("Filter", "water");
                CategoryEvents catEvents = new CategoryEvents();
                catEvents.setArguments(mBundle);
                android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.content_frame, catEvents);
                transaction.addToBackStack("Water Games");
                transaction.commit();
            }
        });

        //ImageButton moreButton;
        campingButton = (ImageButton) v.findViewById(R.id.b_camping);
        campingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {
                /** Fabric **/
                Answers.getInstance().logCustom(new CustomEvent("Categories_Discover")
                        .putCustomAttribute("Category", "Camps"));
                mBundle = new Bundle();
                mBundle.putString("Filter", "camp");
                CategoryEvents catEvents = new CategoryEvents();
                catEvents.setArguments(mBundle);
                android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.content_frame, catEvents);
                transaction.addToBackStack("Camping");
                transaction.commit();
            }
        });

        //ImageButton moreButton;
        bowlingButton = (ImageButton) v.findViewById(R.id.b_bowling);
        bowlingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {
                /** Fabric **/
                Answers.getInstance().logCustom(new CustomEvent("Categories_Discover")
                        .putCustomAttribute("Category", "Bowling"));
                mBundle = new Bundle();
                mBundle.putString("Filter", "bowling");
                CategoryEvents catEvents = new CategoryEvents();
                catEvents.setArguments(mBundle);
                android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.content_frame, catEvents);
                transaction.addToBackStack("Bowling");
                transaction.commit();
            }
        });

        return v;

    }





}
