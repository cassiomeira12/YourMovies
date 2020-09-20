package com.app.yourmovies.utils.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class Adapter<T> extends RecyclerView.Adapter implements View.OnClickListener, View.OnLongClickListener {
    private final String TAG = getClass().getSimpleName();

    public List<T> itensList;
    protected List<T> itensHidden = new ArrayList<>();
    public List<T> itensSelected = new ArrayList<>();

    protected Context context;
    protected Actions actions;

    public T objectSelected;

    public Adapter(@NonNull List<T> itensList, @NonNull Context context, @NonNull Actions actions) {
        this.itensList = itensList;
        this.context = context;
        this.actions = actions;
    }

    abstract public Boolean update(T item);

    abstract public String searchValue(T item);

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick");
        objectSelected = get((int) view.getTag());

        if (!itensSelected.isEmpty()) {
            if (itensSelected.contains(objectSelected)) {
                itensSelected.remove(objectSelected);
                objectSelected = null;
                //Nao executar onClick quando for o ultimo item
                //if (itensSelected.isEmpty()) {
                //    notifyDataSetChanged();
                //    return;
                //}
            } else {
                itensSelected.add(objectSelected);
            }
        }

        //notifyDataSetChanged();
        notifyItemChanged((int) view.getTag());
        this.actions.onClickItem(view);
    }

    @Override
    public boolean onLongClick(View view) {
        Log.d(TAG, "onLongClick");
        objectSelected = get((int) view.getTag());

        if (itensSelected.contains(objectSelected)) {
            itensSelected.remove(objectSelected);
            objectSelected = null;
        } else {
            itensSelected.add(objectSelected);
        }

        this.actions.onLongClickItem(view);

        //notifyDataSetChanged();
        notifyItemChanged((int) view.getTag());
        return true;
    }

    public void notifyItemSelectedDataChanged() {
        if (objectSelected != null) {
            notifyItemChanged(itensList.indexOf(objectSelected));
        }
    }

    @Override
    public int getItemCount() {
        return itensList.size();
    }

    public void search(String query) {
        Log.d(TAG, "search query: " + query);
        itensList.addAll(itensHidden);
        itensHidden.clear();

        query = query.toUpperCase();
        for (T t : itensList) {
            if (!searchValue(t).toUpperCase().contains(query)) {
                itensHidden.add(t);
            }
//            if (!t.toString().toUpperCase().contains(query)) {
//                itensHidden.add(t);
//            }
        }

        for (T t : itensHidden) {
            itensList.remove(t);
        }

        notifyDataSetChanged();
    }

    public Boolean isEmpty() {
        return itensList.isEmpty();
    }

    public void add(T item) {
        itensList.add(item);
        notifyDataSetChanged();
    }

    public void remove(T item) {
        itensList.remove(item);
        notifyDataSetChanged();
    }

    public boolean isItensSelectedEmpty() {
        return itensSelected.isEmpty();
    }

    public void clearItensSelected() {
        itensSelected.clear();
        notifyDataSetChanged();
    }

    public T get(int index) {
        return itensList.isEmpty() ? null : itensList.get(index);
    }

    public interface Actions {
        void onClickItem(View view);
        void onLongClickItem(View view);
    }

}
