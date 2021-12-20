package ch.jentzsch.bandwurm.Helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ch.jentzsch.bandwurm.Classes.HighScore;
import ch.jentzsch.bandwurm.R;

//List Adapter Klasse für den RecyclerView
public class HighScoreListAdapter extends RecyclerView.Adapter<HighScoreListAdapter.ListViewHolder> {
    private LayoutInflater inflater;
    private List<HighScore> highScoreList;
    private Context context;

    public HighScoreListAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    //Punktzahl Liste setzen
    public void setHighScoreList(List<HighScore> highScoreList) {
        this.highScoreList = highScoreList;
        notifyDataSetChanged();
    }

    //Layout für die Items setzen
    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = inflater.inflate(R.layout.row_item, parent, false);
        return new ListViewHolder(itemView);
    }

    //Daten an den RecyclerView anbinden
    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        if (highScoreList == null)
            return;

        final HighScore highScore = highScoreList.get(position);
        if (highScore != null) {
            holder.itemName.setText(highScore.getName());
            holder.itemScore.setText(Integer.toString(highScore.getScore()));
        }
    }

    //Items zählen Methode
    @Override
    public int getItemCount() {
        if (highScoreList == null)
            return 0;
        else
            return highScoreList.size();
    }

    //ViewHolder um TextViews zu instanzieren
    static class ListViewHolder extends RecyclerView.ViewHolder {
        private TextView itemName;
        private TextView itemScore;

        public ListViewHolder(View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.item_name);
            itemScore = itemView.findViewById(R.id.item_score);
        }
    }
}
