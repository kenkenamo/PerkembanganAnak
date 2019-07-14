package com.niken.tkalfiizzah.activity.chooseractivity;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

import com.niken.tkalfiizzah.BR;
import com.niken.tkalfiizzah.R;
import com.niken.tkalfiizzah.base.BaseActivity;
import com.niken.tkalfiizzah.base.OnExecuteListener;
import com.niken.tkalfiizzah.databinding.ActivityChooserBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Firman on 7/7/2019.
 */
public class ChooserActivity extends BaseActivity<ActivityChooserBinding, ChooserViewModel>
        implements OnExecuteListener<List> {

    private List<Object> objectList = new ArrayList<>();
    private ChooserAdapter adapter;
    private ChooserViewModel viewModel;

    public static void startActivityForResult(Activity context, int requestCode, int type) {
        Intent starter = new Intent(context, ChooserActivity.class);
        starter.putExtra("TYPE", type);
        context.startActivityForResult(starter, requestCode);
    }

    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_chooser;
    }

    @Override
    public ChooserViewModel getViewModel() {
        if (viewModel == null)
            viewModel = new ChooserViewModel(this, this);
        return viewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(getBinding().toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new ChooserAdapter(objectList);
        getBinding().listData.setAdapter(adapter);

        int type = getIntent().getIntExtra("TYPE", 0);
        viewModel.setType(type);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chooser, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);


        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();


        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Kode, Nama");
        searchView.setOnQueryTextListener(viewModel.queryTextListener());

        searchView.setOnCloseListener(() -> {

            return true;
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.action_save:
                Intent intent = new Intent();
                if (getIntent().getIntExtra("TYPE", 0) == 0)
                    intent.putExtra("data", adapter.getSelectedUser());
                else
                    intent.putExtra("data", adapter.getSelectedSiswa());

                setResult(RESULT_OK, intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onExecuted(List objects) {
        this.objectList.clear();
        this.objectList.addAll(objects);
        adapter.updateDataSet();
    }

    @Override
    public void onError(Throwable throwable) {

    }
}
