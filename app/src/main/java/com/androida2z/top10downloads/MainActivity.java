package com.androida2z.top10downloads;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ListView listApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listApps = (ListView) findViewById(R.id.xmlListview);

        Log.d(TAG, "onCreate: Starting async");
        DownloadData downloadData = new DownloadData();
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=200/xml");
        Log.d(TAG, "onCreate: done");


    }


    private class DownloadData extends AsyncTask<String , Void, String>{

        private static final String TAG = "DoanloadData";
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.d(TAG, "onPostExecute: parameter is " + s);

            ParseApplications parseApplications = new ParseApplications();
            parseApplications.parse(s);

//            ArrayAdapter<FeedEntry> arrayAdapter = new ArrayAdapter<FeedEntry>(MainActivity.this, R.layout.list_item, parseApplications.getApplications());
//
//            listApps.setAdapter(arrayAdapter);

            FeedAdapter feedAdapter = new FeedAdapter(MainActivity.this, R.layout.list_item, parseApplications.getApplications());

            listApps.setAdapter(feedAdapter);

        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: starts with " + strings[0]);
            
            String rssFeed = downloadXML(strings[0]);
            
            if (rssFeed == null){
                Log.e(TAG, "doInBackground: DownloadData error " );
            }
            
            return rssFeed;
        }

        private String downloadXML(String urlPath){

            StringBuilder xmlResults = new StringBuilder();

            try{

                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                Log.d(TAG, "downloadXML: Response code was " + response);


//                InputStream inputStream = connection.getInputStream();
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader reader = new BufferedReader(inputStreamReader);

                BufferedReader reader =  new BufferedReader(new InputStreamReader(connection.getInputStream()));


                int charsRead;

                char[] inputBuffer = new char[500];
                while(true){

                    charsRead = reader.read(inputBuffer);
                    if(charsRead < 0 ){
                        break;
                    }
                    if(charsRead > 0){
                        xmlResults.append(String.copyValueOf(inputBuffer, 0, charsRead));
                    }

                }
                reader.close();

                return xmlResults.toString();
            }catch (MalformedURLException e){
                Log.e(TAG, "downloadXML: Invalid URL" + e.getMessage());
                
            }catch (IOException e){
                Log.e(TAG, "downloadXML: IO Exception reading data" + e.getMessage() );
            }
            catch (SecurityException e){
                Log.e(TAG, "downloadXML: Security exception: need permissions" + e.getMessage() );
            }

            return null;
        }
    }

}
