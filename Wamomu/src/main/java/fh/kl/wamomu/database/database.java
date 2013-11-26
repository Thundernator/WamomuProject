package fh.kl.wamomu.database;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fh.kl.wamomu.R;

public class database extends Activity {
    private String jsonResult;
//    private String url = "http://cpriyankara.coolpage.biz/employee_details.php";


    protected int usersID;
    private String url = "http://192.168.178.48/wamomusql/users_details.php";
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView1);
        accessWebService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void setUsersID(int usersID) {
        this.usersID = usersID;
    }

    public int getUsersID() {
        return usersID;
    }

    // Async Task to access the web
    private class JsonReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            System.out.println("injsonReadTask rly?" + params[0]);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);

            try {
                HttpResponse response = httpclient.execute(httppost);
//                System.out.println("Test#############" + inputStreamToString(
//                        response.getEntity().getContent()).toString());
                jsonResult = inputStreamToString(
                        response.getEntity().getContent()).toString();
                System.out.println("Test#########" + jsonResult);

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            try {
                while ((rLine = rd.readLine()) != null) {
                    answer.append(rLine);
                }
            } catch (IOException e) {
                // e.printStackTrace();
                System.out.println("Error: " + e.toString());
                Toast.makeText(getApplicationContext(),
                        "Error..." + e.toString(), Toast.LENGTH_LONG).show();
            }
            return answer;
        }

        @Override
        protected void onPostExecute(String result) {
//            ListDrwaer();
        }
    }// end async task

    public void accessWebService() {
        JsonReadTask task = new JsonReadTask();
        // passes values for the urls string array
        task.execute(new String[]{url});
        System.out.println("##########AccessWebService#########= ");

    }

    // build hash set for list view
    public void ListDrwaer() {
        List<Map<String, String>> employeeList = new ArrayList<Map<String, String>>();

        try {
            JSONObject jsonResponse = new JSONObject(jsonResult);

            System.out.println("JSONObject= " + jsonResponse.toString());
            JSONArray jsonMainNode = jsonResponse.optJSONArray("users");
            System.out.println("JSONONArray= " + jsonMainNode.toString());


            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                System.out.println("jsonChildNode= " + jsonChildNode.toString());
                String test = jsonChildNode.getString("user");
                System.out.println("Current User= " + test);
                String number = jsonChildNode.optString("id");
                System.out.println("id= " + number);

                String user = jsonChildNode.optString("user");
                System.out.println("user" + user);

                String password = jsonChildNode.optString("password");
                System.out.println("Password" + password);

                String outPut = user + "-" + number + "-" + password;
                employeeList.add(createEmployee("employees", outPut));
            }
        } catch (JSONException e) {
            Toast.makeText(this, "Error" + e.toString(),
                    Toast.LENGTH_SHORT).show();
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, employeeList,
                android.R.layout.simple_list_item_1,
                new String[]{"employees"}, new int[]{android.R.id.text1});
        listView.setAdapter(simpleAdapter);
    }

    private HashMap<String, String> createEmployee(String user, String number) {
        HashMap<String, String> employeeuserNo = new HashMap<String, String>();
        employeeuserNo.put(user, number);
        return employeeuserNo;
    }

    public boolean checkUser(String useruser, String userpassword) {
        boolean datatrue = false;

        try {
            JSONObject jsonResponse = new JSONObject(jsonResult);
            System.out.println("jsonresult= " + jsonResult);
            System.out.println("JSONObject= " + jsonResponse.toString());
            JSONArray jsonMainNode = jsonResponse.optJSONArray("users");
            System.out.println("jsonResponse.optJSONArray= " + jsonMainNode.toString());

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                System.out.println("Current User= " + jsonChildNode.toString());
                String number = jsonChildNode.optString("id");
                System.out.println("ID=  " + number);
                String user = jsonChildNode.optString("user");
                System.out.println("user: " + user);
                String password = jsonChildNode.optString("password");
                System.out.println("Password: " + password);

                if (useruser.equals(user) && userpassword.equals(password)) {
                    System.out.println("DATATRUE!!!!!!!!!!!!!!!!!!!");
                    datatrue = true;
                    setUsersID(Integer.parseInt(number));
                } else {
                    System.out.println("DATAFALSEE!!!!!!!!!!!!!!lllllllllllllllllllllllllllllllllllllllllllllll");
                }
            }
        } catch (JSONException e) {
            System.out.println(e.toString());
        }

        return datatrue;
    }
}
