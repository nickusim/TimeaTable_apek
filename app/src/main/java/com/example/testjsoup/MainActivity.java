package com.example.testjsoup;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    public ArrayList<String> titleList = new ArrayList<String>();
    public Elements content;
    private ArrayAdapter<String> adapter;
    List<String> listURL = new ArrayList<String>();

    ListView lstView;
    String URL = "http://portal.asiec.ru/timetable/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lstView = (ListView) findViewById(R.id.lstView);

        new NewThread().execute();
        adapter = new ArrayAdapter<String>(this,R.layout.list_item,R.id.pro_item,titleList);

        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("URI","position: " + position + ", id: " + id + ", URL: " + listURL.get(position));
                Intent downloadFile = new Intent(Intent.ACTION_VIEW, Uri.parse(listURL.get(position)));
                startActivity(downloadFile);

            }
        });

    }
    public class NewThread extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... args){
            Document doc;
            try{
                doc = Jsoup.connect(URL).get();
                content = doc.select("a.element-title");
                titleList.clear();
                for (Element contents: content){
                    titleList.add(contents.text());
                    listURL.add(contents.absUrl("data-bx-download"));
                    Log.d("Downloads", contents.absUrl("data-bx-download"));
                }
            } catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result){
            lstView.setAdapter(adapter);
        }
    }
}
