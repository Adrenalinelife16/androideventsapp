package com.adrenalinelife.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.adrenalinelife.Browser;
import com.adrenalinelife.CreateEvent;
import com.adrenalinelife.DiscoverBrowser;
import com.adrenalinelife.R;
import com.adrenalinelife.custom.CustomFragment;

public class DiscoverEvents extends CustomFragment {

    ImageButton yogaButton;
    ImageButton basketballButton;
    ImageButton soccerButton;
    ImageButton frisbeeButton;
    ImageButton golfButton;
    ImageButton tennisButton;
    ImageButton fishingButton;
    ImageButton moreButton;
    ImageButton kickballButton;
    ImageButton tablegamesButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View v = inflater.inflate(R.layout.discover_events, null);

        //yoga
        yogaButton = (ImageButton) v.findViewById(R.id.b_yoga);
        yogaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {
                startActivity(new Intent(getActivity(),
                        CreateEvent.class));
            }
        });

        //ImageButton basketballButton;
        basketballButton = (ImageButton) v.findViewById(R.id.b_basketball);
        basketballButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {
                String url = "http://www.AdrenalineLife.org/events/categories/basketball/";
                Intent intent = new Intent(view.getContext(), Browser.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });

        //ImageButton soccerButton;
        soccerButton = (ImageButton) v.findViewById(R.id.b_soccer);
        soccerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {
                String url = "http://www.AdrenalineLife.org/events/categories/soccer/";
                Intent intent = new Intent(view.getContext(), Browser.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });


        //ImageButton golfButton;
        golfButton = (ImageButton) v.findViewById(R.id.b_golf);
        golfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {
                String url = "http://www.AdrenalineLife.org/events/categories/golf/";
                Intent intent = new Intent(view.getContext(), Browser.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });

        //ImageButton frisbeeButton;
        frisbeeButton = (ImageButton) v.findViewById(R.id.b_frisbee);
        frisbeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {
                String url = "http://www.AdrenalineLife.org/events/categories/frisbee/";
                Intent intent = new Intent(view.getContext(), Browser.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });

        //ImageButton tennisButton;
        tennisButton = (ImageButton) v.findViewById(R.id.b_tennis);
        tennisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {
                String url = "http://www.AdrenalineLife.org/events/categories/tennis/";
                Intent intent = new Intent(view.getContext(), Browser.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });

        //ImageButton fishingButton;
        fishingButton = (ImageButton) v.findViewById(R.id.b_fishing);
        fishingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {
                String url = "http://www.AdrenalineLife.org/events/categories/fishing/";
                Intent intent = new Intent(view.getContext(), Browser.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });


        //ImageButton moreButton;
        moreButton = (ImageButton) v.findViewById(R.id.b_more);
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {
                String url = "http://www.AdrenalineLife.org/events/categories/";
                Intent intent = new Intent(view.getContext(), Browser.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });

        //ImageButton kickballButton;
        kickballButton = (ImageButton) v.findViewById(R.id.b_kickball);
        kickballButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {
                String yoga = "http://www.AdrenalineLife.org/events/categories/kickball/";
                Intent intent = new Intent(view.getContext(), Browser.class);
                intent.putExtra("url", yoga);
                startActivity(intent);
            }
        });

        //ImageButton tablegamesButton;
        tablegamesButton = (ImageButton) v.findViewById(R.id.b_tablegames);
        tablegamesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {
                String yoga = "http://www.AdrenalineLife.org/events/categories/tablegames/";
                Intent intent = new Intent(view.getContext(), Browser.class);
                intent.putExtra("url", yoga);
                startActivity(intent);
            }
        });
        return v;
    }
}
