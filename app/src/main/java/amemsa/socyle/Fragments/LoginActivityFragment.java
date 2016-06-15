package amemsa.socyle.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
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

import amemsa.socyle.Activities.RegistrationActivity;
import amemsa.socyle.Activities.WelcomeActivity;
import amemsa.socyle.Pojo.User;
import amemsa.socyle.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginActivityFragment extends Fragment {

    private Button login;
    private TextView signup;
    private EditText email;
    private EditText password;
    private String mail;
    private String pass;

    private User user;

    private SharedPreferences data;
    private SharedPreferences.Editor dataE;
    private CheckBox rememberMe;

    public LoginActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        login = (Button) rootView.findViewById(R.id.login);
        signup = (TextView) rootView.findViewById(R.id.sign_up);
        email = (EditText) rootView.findViewById(R.id.email);
        password = (EditText) rootView.findViewById(R.id.password);
        rememberMe = (CheckBox) rootView.findViewById(R.id.remember);

        user = new User();


        data = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        dataE = data.edit();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mail = email.getText().toString();
                pass = password.getText().toString();
                login();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RegistrationActivity.class));
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!data.getString("email", "").equals("") && !data.getString("pass", "").equals("")) {
            rememberMe.setChecked(true);
            email.setText(data.getString("email", ""));
            password.setText(data.getString("pass", ""));
        } else {
            rememberMe.setChecked(false);
            email.setText("");
            password.setText("");
        }
    }


    private void login() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        final String url = "http://socyle.com/intership/index.php/login";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject loginResponseJson = new JSONObject(response);
                            JSONObject jsonResult = (JSONObject) loginResponseJson.get("data");
                            int result = Integer.parseInt(jsonResult.get("result").toString());
                            if (result == 1) {
                                if (rememberMe.isChecked()) {
                                    if (!data.getString("email", "").equals(mail)) {
                                        dataE.putString("email", mail);
                                        dataE.putString("pass", pass);
                                        dataE.commit();
                                    }
                                } else {
                                    if (data.getString("email", "").equals(mail)) {
                                        dataE.putString("email", "");
                                        dataE.putString("pass", "");
                                        dataE.commit();
                                    }
                                }
                                user.setEmail(mail);
                                user.setPassword(pass);
                                Intent i = new Intent(getActivity(), WelcomeActivity.class);
                                i.putExtra("user",user);
                                startActivity(i);
                                getActivity().finish();
                            } else {
                                Toast.makeText(getActivity(), "Login Error !\ntry again.", Toast.LENGTH_LONG).show();

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
                    userLoginJson.put("email", mail);
                    userLoginJson.put("password", pass);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(userLoginJson.toString());
                params.put("login", userLoginJson.toString());
                return params;
            }
        };

        queue.add(request);

    }

}
