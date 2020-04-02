package be.uhasselt.drain;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListDetailFragment extends Fragment {

    TextView detTitle;
    TextView detDescription;
    ImageView detImage;

    Drink drink;

    public ListDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_detail, container, false);

        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // When item is added to bundle: read bundle, otherwise from intent
        Bundle bundle = getArguments();
        drink = new Drink();
        if (bundle != null) {
            drink = (Drink) getArguments().getSerializable("Drink");
        } else {
            Intent intent = getActivity().getIntent();
            this.drink = (Drink) intent.getSerializableExtra("Drink");
            if (drink == null) {
                drink = new Drink();
            }
        }

        detTitle = (TextView) getView().findViewById(R.id.det_title);
        detDescription = (TextView) getView().findViewById(R.id.det_description);
        detImage = (ImageView) getView().findViewById(R.id.det_image);

        detTitle.setText(drink.getName());
        detDescription.setText(drink.getAmount() + " ml");
        Picasso.get().load(drink.getImage()).into(detImage);
    }
}
