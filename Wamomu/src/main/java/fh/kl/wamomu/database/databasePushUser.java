package fh.kl.wamomu.database;


import android.os.AsyncTask;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import fh.kl.wamomu.ui.Register;

public class databasePushUser {
    private String jsonResult;

    private String url = "http://" + database.ip + "/wamomusql/adduser.php";

    /**
     * Async Task die via HTTP Post den JSON String herunterlädt
     */
    private class JsonReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);

            try {
                HttpResponse response = httpclient.execute(httppost);
                jsonResult = inputStreamToString(
                        response.getEntity().getContent()).toString();

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private StringBuilder inputStreamToString(InputStream is) {
            String rLine;
            StringBuilder answer = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            try {
                while ((rLine = rd.readLine()) != null) {
                    answer.append(rLine);
                }
            } catch (IOException e) {
                 e.printStackTrace();
            }
            return answer;
        }
    }// end async task
    /**
     * startet die AsycTask, die den JSON String von der url parsed, die Angaben der PHP-File übergibt und
     * in die Datenbank hinzufügt
     */
    public void accessWebService() {
        JsonReadTask task = new JsonReadTask();
        // passes values for the urls string array
        task.execute(new String[]{url + "?vname=" + Register.getStrVname() + "&nname=" + Register.getStrName() + "&benutzername=" + Register.getStrUsername() + "&passwort=" + Register.getStrPassword() + "&passwortrepeat=" + Register.getStrpasswordRepeat()});
    }
}
