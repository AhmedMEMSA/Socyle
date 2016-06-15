package amemsa.socyle.Fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import amemsa.socyle.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class RegistrationActivityFragment extends Fragment {

    private EditText nameET;
    private EditText passwordET;
    private EditText emailET;
    private EditText phoneET;
    private Button signup;
    private String name;
    private String password;
    private String email;
    private String phone;

    public RegistrationActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_registation, container, false);
        nameET = (EditText) rootView.findViewById(R.id.r_name);
        passwordET = (EditText) rootView.findViewById(R.id.r_password);
        emailET = (EditText) rootView.findViewById(R.id.r_email);
        phoneET = (EditText) rootView.findViewById(R.id.r_phone);
        signup = (Button) rootView.findViewById(R.id.r_sign_up);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameET.getText().toString();
                password = passwordET.getText().toString();
                email = emailET.getText().toString();
                phone = phoneET.getText().toString();
                signup();

            }
        });

        return rootView;
    }

    private void signup() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        final String url = "http://socyle.com/intership/index.php/register";
        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            int result;
                            JSONObject loginResponseJson = new JSONObject(response);
                            System.out.println(loginResponseJson);
                            JSONObject jsonResult = (JSONObject) loginResponseJson.get("data");
                            result = Integer.parseInt(jsonResult.get("result").toString());
                            if (result == 1) {
                                Toast.makeText(getActivity(), "Done", Toast.LENGTH_LONG).show();
                                getActivity().finish();
                            } else {
                                Toast.makeText(getActivity(), "Error, Try again", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                JSONObject userLoginJson = new JSONObject();
                try {
                    userLoginJson.put("username", name);
                    userLoginJson.put("email", email);
                    userLoginJson.put("password", password);
                    userLoginJson.put("phone", phone);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                params.put("user", userLoginJson.toString());
                return params;
            }
        };

        queue.add(request);
    }
}
