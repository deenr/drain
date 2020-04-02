package be.uhasselt.drain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class StatAdapter extends RecyclerView.Adapter<StatAdapter.StatViewHolder> {

    Context context;
    private ArrayList<String> titleData, descriptionData;

    public StatAdapter(Context context, ArrayList<String> titleData, ArrayList<String> descriptionData) {
        this.context = context;
        this.titleData = titleData;
        this.descriptionData = descriptionData;
    }

    @NonNull
    @Override
    public StatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.stat_cardview, viewGroup, false);
        return new StatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatViewHolder holder, int position) {
        holder.title.setText(titleData.get(position));
        holder.description.setText(descriptionData.get(position));
    }

    @Override
    public int getItemCount() {
        return titleData.size();
    }

    class StatViewHolder extends RecyclerView.ViewHolder {

        TextView title, description;

        public StatViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.rv_stat_title);
            description = (TextView) itemView.findViewById(R.id.rv_stat_description);
        }
    }
}
