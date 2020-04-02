package be.uhasselt.drain;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Drink> drinkProfile;

    public MyAdapter(Context context, ArrayList<Drink> drinkProfile) {
        this.context = context;
        this.drinkProfile = drinkProfile;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_cardview, viewGroup, false);
        final MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txtTitle.setText(drinkProfile.get(position).getName());
        holder.txtDescription.setText(drinkProfile.get(position).getAmount() + " ml");
        Picasso.get().load(drinkProfile.get(position).getImage()).into(holder.myImage);
    }

    @Override
    public int getItemCount() {
        return drinkProfile.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtTitle, txtDescription;
        ImageView myImage;
        ConstraintLayout listLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = (TextView) itemView.findViewById(R.id.rv_title);
            txtDescription = (TextView) itemView.findViewById(R.id.rv_description);
            myImage = (ImageView) itemView.findViewById(R.id.rv_image);
            listLayout = (ConstraintLayout) itemView.findViewById(R.id.listLayout);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Log.d("Click", String.valueOf(position));

            FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
            ListDetailFragment detailFragment = (ListDetailFragment) fm.findFragmentById(R.id.detail_fragment);

            Drink drink = drinkProfile.get(position);

            if (detailFragment != null && detailFragment.isVisible()) {
                // Visible: create new fragment & send bundle with data
                ListDetailFragment newFragment = new ListDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("Drink", drink);
                newFragment.setArguments(bundle);

                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(detailFragment.getId(), newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            } else {
                // Not visible: start as intent
                Intent intent = new Intent(itemView.getContext(), ListDetailActivity.class);
                intent.putExtra("Drink", drink);
                itemView.getContext().startActivity(intent);
            }
        }
    }
}
