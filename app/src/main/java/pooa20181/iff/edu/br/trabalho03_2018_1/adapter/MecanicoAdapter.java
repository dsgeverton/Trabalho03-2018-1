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
import pooa20181.iff.edu.br.trabalho03_2018_1.model.Mecanico;

public class MecanicoAdapter extends RecyclerView.Adapter{

    private List<Mecanico> mecanicos;
    private Context context;
    private ClickRecyclerViewListener clickRecyclerViewListener;

    public MecanicoAdapter(List<Mecanico> mecanicos, Context context, ClickRecyclerViewListener clickRecyclerViewListener) {
        this.mecanicos = mecanicos;
        this.context = context;
        this.clickRecyclerViewListener = clickRecyclerViewListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.cardview_mecanico, parent, false);
        MecanicoAdapter.MecanicoViewHolder mecanicoViewHolder = new MecanicoViewHolder(view);
        return mecanicoViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MecanicoAdapter.MecanicoViewHolder mViewHolder = (MecanicoAdapter.MecanicoViewHolder) holder;
        Mecanico mecanico = mecanicos.get(position);
        mViewHolder.nomeMecanico.setText(mecanico.getNome());
        mViewHolder.funcaoMecanico.setText(mecanico.getFuncao());
    }

    @Override
    public int getItemCount() {
        return mecanicos.size();
    }

    public class MecanicoViewHolder extends RecyclerView.ViewHolder {
        private final TextView nomeMecanico;
        private final TextView funcaoMecanico;
        public MecanicoViewHolder(View itemView) {
            super(itemView);
            nomeMecanico = itemView.findViewById(R.id.tvNomeMecanico);
            funcaoMecanico = itemView.findViewById(R.id.tvFuncaoMecanico);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickRecyclerViewListener.onClick(mecanicos.get(getLayoutPosition()));
                }
            });
        }
    }
}

