package codezilla.foss.nirogya.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import codezilla.foss.nirogya.Authenticated;
import codezilla.foss.nirogya.R;
import codezilla.foss.nirogya.activities.TwitterLoginActivity;
import codezilla.foss.nirogya.database.NirogyaDataSource;


/**
 * Created by chinthaka on 5/17/18.
 */

public class TipsFragment extends Fragment {

    private final static String screenName = "nirogya";
    private TextView captionTextView, descriptionTextView;
    private Button viewMoreTweets;
    private final static String consumerKey = "nW88XLuFSI9DEfHOX2tpleHbR";
    private final static String consumerSecret = "hCg3QClZ1iLR13D3IeMvebESKmakIelp4vwFUICuj6HAfNNCer";
    private final static String twitterTokenUrl = "https://api.twitter.com/oauth2/token";
    private final static String twitterStreamUrl = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=";
    private ProgressDialog progressDialog;
    private NirogyaDataSource nirogyaDataSource;
    private long diffDays;
    private String simpleDateFormat = "yyyy/MM/dd HH:mm:ss";
    private String defaultTableRowIndexValue = "1";
    private String hourText = "Hours";
    private String minuteText = "Minutes &";
    private String secondText = "Seconds";
    private String simpleTimeFormat = "HH:mm:ss";
    private String marginTime = "24:00:00";
    private String retrievedObjectJsonKey = "text";
    private String twitter_data_consumer_key_encode_type = "UTF-8";
    private String twitter_data_consumer_secret_encode_type = "UTF-8";
    private String twitter_data_http_post_header1_name = "Authorization";
    private String twitter_data_http_post_header1_value = "Basic ";
    private String twitter_data_http_post_header2_name = "Content-Type";
    private String twitter_data_http_post_header2_value = "application/x-www-form-urlencoded;charset=UTF-8";
    private String twitter_data_auth_token_type = "bearer";
    private String twitter_data_http_post_string_entity = "grant_type=client_credentials";
    private String twitter_data_http_get_header1_name = "Authorization";
    private String twitter_data_http_get_header1_value = "Bearer ";
    private String twitter_data_http_get_header2_name = "Content-Type";
    private String twitter_data_http_get_header2_value = "application/json";
    private String twitter_data_response_divide_pattern = "---";


    public static TipsFragment newInstance() {
        TipsFragment fragment = new TipsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nirogyaDataSource = new NirogyaDataSource(getActivity());
        nirogyaDataSource.open();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment_tips = inflater.inflate(R.layout.fragment_tips, container, false);
        captionTextView = fragment_tips.findViewById(R.id.caption);
        descriptionTextView = fragment_tips.findViewById(R.id.description);
        viewMoreTweets = fragment_tips.findViewById(R.id.view_more_tweets);
        checkDayGap();
        viewMoreTweets.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TwitterLoginActivity.class);
                startActivity(intent);
            }
        });

        return fragment_tips;
    }

    // download twitter timeline after first checking to see if there is a network connection
    public void downloadTweets() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            new TipsFragment.DownloadTwitterTask().execute(screenName);
        } else {
            Toast.makeText(getActivity(), getString(R.string.tips_fragment_check_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(getActivity(), R.style.Theme_Dialog);
        progressDialog.setMessage(getResources().getString(R.string.tips_fragment_progress_dialog_title)); // Setting Message
        progressDialog.setTitle(getResources().getString(R.string.tips_fragment_progress_dialog_message)); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
    }

    private void checkDayGap() {

        DateFormat dateFormat = new SimpleDateFormat(simpleDateFormat);
        Date date = new Date();

        String[] retrievedData = nirogyaDataSource.getAllDataFromTwitterData(defaultTableRowIndexValue);
        String caption = retrievedData[1];
        String dateStart = retrievedData[0];
        String dateStop = dateFormat.format(date);

        //HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat format = new SimpleDateFormat(simpleDateFormat);

        Date d1 = null;
        Date d2 = null;


        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);
            //in milliseconds
            long diff = d2.getTime() - d1.getTime();

            final long diffSeconds = diff / 1000 % 60;
            final long diffMinutes = diff / (60 * 1000) % 60;
            final long diffHours = diff / (60 * 60 * 1000) % 24;
            diffDays = diff / (24 * 60 * 60 * 1000);


            if (caption == null) {
                showProgressDialog();
                downloadTweets();
            } else {
                if (diffDays >= 1) {
                    showProgressDialog();
                    downloadTweets();
                } else {
                    captionTextView.setText(retrievedData[1]);
                    descriptionTextView.setText(retrievedData[2]);
                    SimpleDateFormat fr = new SimpleDateFormat(simpleTimeFormat);
                    Date t1 = fr.parse(marginTime);
                    Date t2 = fr.parse(diffHours + ":" + diffMinutes + ":" + diffSeconds);
                    long timeDiffer = t1.getTime() - t2.getTime();
                    final long Seconds = timeDiffer / 1000 % 60;
                    final long Minutes = timeDiffer / (60 * 1000) % 60;
                    final long Hours = timeDiffer / (60 * 60 * 1000) % 24;
                    String timeDifference = Hours + " " + hourText + " " + Minutes + " " + minuteText + " " + Seconds + " " + secondText + " ";

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_Dialog);
                    builder.setTitle(getResources().getString(R.string.tips_fragment_alert_dialog_title))
                            .setMessage(getResources().getString(R.string.tips_fragment_alert_dialog_message) + " " + timeDifference)
                            .setCancelable(false)
                            .setPositiveButton(getResources().getString(R.string.tips_fragment_alert_dialog_ok_button_text), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Uses an AsyncTask to download a Twitter user's timeline
    private class DownloadTwitterTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... screenNames) {
            String result = null;
            if (screenNames.length > 0) {
                result = getTwitterStream(screenNames[0]);
            }
            return result;
        }

        // onPostExecute convert the JSON results into a Twitter object (which is an Array list of tweets
        @Override
        protected void onPostExecute(String result) {
            Log.e("result", result);
            //   nirogyaDataSource.updateDataTwitterData();
            progressDialog.dismiss();

            try {
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String tweet = jsonObject.getString(retrievedObjectJsonKey);
                String[] division = tweet.split(twitter_data_response_divide_pattern);
                String caption = division[0];
                String description = division[1];
                DateFormat dateFormat = new SimpleDateFormat(simpleDateFormat);
                Date date = new Date();
                String dateNow = dateFormat.format(date);
                nirogyaDataSource.updateDataTwitterData(defaultTableRowIndexValue, dateNow, caption, description);
                captionTextView.setText(caption);
                descriptionTextView.setText(description);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // convert a JSON authentication object into an Authenticated object
        private Authenticated jsonToAuthenticated(String rawAuthorization) {
            Authenticated auth = null;
            if (rawAuthorization != null && rawAuthorization.length() > 0) {
                try {
                    Gson gson = new Gson();
                    auth = gson.fromJson(rawAuthorization, Authenticated.class);
                } catch (IllegalStateException ex) {
                    // just eat the exception
                }
            }
            return auth;
        }

        private String getResponseBody(HttpRequestBase request) {
            StringBuilder sb = new StringBuilder();
            try {
                DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
                HttpResponse response = httpClient.execute(request);
                int statusCode = response.getStatusLine().getStatusCode();
                String reason = response.getStatusLine().getReasonPhrase();

                if (statusCode == 200) {

                    HttpEntity entity = response.getEntity();
                    InputStream inputStream = entity.getContent();

                    BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    String line;
                    while ((line = bReader.readLine()) != null) {
                        sb.append(line);
                    }
                } else {
                    sb.append(reason);
                }
            } catch (UnsupportedEncodingException ex) {
            } catch (ClientProtocolException ex1) {
            } catch (IOException ex2) {
            }
            return sb.toString();
        }

        private String getTwitterStream(String screenName) {
            String results = null;

            // Step 1: Encode consumer key and secret
            try {
                // URL encode the consumer key and secret
                String urlApiKey = URLEncoder.encode(consumerKey, twitter_data_consumer_key_encode_type);
                String urlApiSecret = URLEncoder.encode(consumerSecret, twitter_data_consumer_secret_encode_type);

                // Concatenate the encoded consumer key, a colon character, and the
                // encoded consumer secret
                String combined = urlApiKey + ":" + urlApiSecret;

                // Base64 encode the string
                String base64Encoded = Base64.encodeToString(combined.getBytes(), Base64.NO_WRAP);

                // Step 2: Obtain a bearer token
                HttpPost httpPost = new HttpPost(twitterTokenUrl);
                httpPost.setHeader(twitter_data_http_post_header1_name, twitter_data_http_post_header1_value + base64Encoded);
                httpPost.setHeader(twitter_data_http_post_header2_name, twitter_data_http_post_header2_value);
                httpPost.setEntity(new StringEntity(twitter_data_http_post_string_entity));
                String rawAuthorization = getResponseBody(httpPost);
                Authenticated auth = jsonToAuthenticated(rawAuthorization);
                // Applications should verify that the value associated with the
                // token_type key of the returned object is bearer
                if (auth != null && auth.token_type.equals(twitter_data_auth_token_type)) {
                    // Step 3: Authenticate API requests with bearer token
                    HttpGet httpGet = new HttpGet(twitterStreamUrl + screenName);
                    // construct a normal HTTPS request and include an Authorization
                    // header with the value of Bearer <>
                    httpGet.setHeader(twitter_data_http_get_header1_name, twitter_data_http_get_header1_value + auth.access_token);
                    httpGet.setHeader(twitter_data_http_get_header2_name, twitter_data_http_get_header2_value);
                    // update the results with the body of the response
                    results = getResponseBody(httpGet);
                }
            } catch (UnsupportedEncodingException ex) {
            } catch (IllegalStateException ex1) {
            }
            return results;
        }
    }

}