package pooa20181.iff.edu.br.trabalho03_2018_1.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import pooa20181.iff.edu.br.trabalho03_2018_1.R;
import pooa20181.iff.edu.br.trabalho03_2018_1.model.Oficina;

public class OficinaAdapter extends RecyclerView.Adapter {

    private List<Oficina> oficinas;
    private Context context;
    private static ClickRecyclerViewListener clickRecyclerViewListener;

    public OficinaAdapter(List<Oficina> oficinas, Context context, ClickRecyclerViewListener clickRecyclerViewListener) {
        this.oficinas = oficinas;
        this.context = context;
        this.clickRecyclerViewListener = clickRecyclerViewListener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.cardview_oficina, parent, false);
        return new OficinaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        OficinaViewHolder v_holder = (OficinaViewHolder) holder;
        Oficina oficina  = oficinas.get(position) ;
        v_holder.nomeOficina.setText(oficina.getNome());
        v_holder.ruaOficina.setText(oficina.getRua());
    }

    @Override
    public int getItemCount() {
        return oficinas.size();
    }

    private class OficinaViewHolder extends RecyclerView.ViewHolder {
        private final TextView nomeOficina;
        private final TextView ruaOficina;
        private OficinaViewHolder(View itemView) {
            super(itemView);
            nomeOficina = itemView.findViewById(R.id.tvNomeOficina);
            ruaOficina = itemView.findViewById(R.id.tvRuaOficina);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickRecyclerViewListener.onClick(oficinas.get(getLayoutPosition()));
                }
            });
        }
    }
}
