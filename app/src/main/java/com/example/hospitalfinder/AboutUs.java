package com.example.hospitalfinder;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AboutUs  extends ListActivity {

    private ListView listView;
    private String profileNames[] = {
            "AMIRUL ADAM \n BIN HAZERIN EZERI ",
            "AMIR SAFWAN BIN MOHD AJNI ",
            "MUHAMMAD SYAHIR HAKIM \nBIN CHE MUDA",
            "ZUL HILMI BIN MAZLAN",
    };

    private String studentId[] = {
            "2021155785",
            "2021190047",
            "2021166953",
            "2021101271",
    };

    private Integer imageid[] = {
            R.drawable.adey,
            R.drawable.amir,
            R.drawable.syahir,
            R.drawable.zh,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        // Setting header
        TextView textView = new TextView(this);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setText("List of Developers");

        ListView listView=(ListView)findViewById(android.R.id.list);
        listView.addHeaderView(textView);

        // For populating list data
        CustomProfileList customCountryList = new CustomProfileList(this, profileNames, studentId, imageid);
        listView.setAdapter(customCountryList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(getApplicationContext(),"You Selected "+profileNames[position-1],Toast.LENGTH_SHORT).show();        }
        });
    }

    public void openBrowser(View view){

        //Get url from tag
        String url = (String)view.getTag();

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);

        //pass the url to intent data
        intent.setData(Uri.parse(url));

        startActivity(intent);
    }
}

