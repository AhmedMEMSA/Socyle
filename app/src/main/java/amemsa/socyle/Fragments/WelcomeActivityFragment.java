package amemsa.socyle.Fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import amemsa.socyle.Pojo.User;
import amemsa.socyle.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class WelcomeActivityFragment extends Fragment {

    private TextView emailTV;

    public WelcomeActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);
        Intent i = getActivity().getIntent();
        User user = i.getParcelableExtra("user");
        emailTV = (TextView)rootView.findViewById(R.id.w_email);
        emailTV.setText(user.getEmail());
        return rootView;
    }
}
