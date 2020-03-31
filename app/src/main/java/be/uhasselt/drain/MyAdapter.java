package be.uhasselt.drain;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private LayoutInflater layoutInflater;
    private ArrayList<Drink> drinkProfile;

    public MyAdapter(Context c, ArrayList<Drink> p){
        this.layoutInflater = LayoutInflater.from(c);
        this.drinkProfile = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_cardview, viewGroup, false);
        return new MyViewHolder(view);
    }

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

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtTitle, txtDescription;
        ImageView myImage;
        ConstraintLayout listLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.rv_title);
            txtDescription = (TextView) itemView.findViewById(R.id.rv_description);
            myImage = (ImageView) itemView.findViewById(R.id.rv_image);
            listLayout = (ConstraintLayout) itemView.findViewById(R.id.listLayout);
        }
    }
}
