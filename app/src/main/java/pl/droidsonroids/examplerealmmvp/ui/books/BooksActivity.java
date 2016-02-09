package pl.droidsonroids.examplerealmmvp.ui.books;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;
import javax.inject.Inject;
import pl.droidsonroids.examplerealmmvp.MyApplication;
import pl.droidsonroids.examplerealmmvp.R;
import pl.droidsonroids.examplerealmmvp.model.Book;
import pl.droidsonroids.examplerealmmvp.ui.adapter.ListAdapter;
import pl.droidsonroids.examplerealmmvp.ui.add.AddBookActivity;
import pl.droidsonroids.examplerealmmvp.ui.detail.DetailActivity;

public class BooksActivity extends AppCompatActivity implements ListView, ListAdapter.OnBookClickListener {

    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;
    @Bind(R.id.toolbar) Toolbar mToolbar;

    @Inject ListPresenter mMyListPresenter;

    private ListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        MyApplication.injectModules(this, new ListModule());

        initToolbar();
        initList();
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
    }

    private void initList() {
        mAdapter = new ListAdapter();
        mAdapter.setOnBookClickListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMyListPresenter.setView(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMyListPresenter.clearView();
    }

    @Override
    protected void onDestroy() {
        mMyListPresenter.closeRealm();
        super.onDestroy();
    }

    @Override
    public void showBooks(final RealmResults<Book> books) {
        mAdapter.setBooks(books);
    }

    @Override
    public void onBookClick(final int id) {
        mMyListPresenter.onBookClick(id);
    }

    @Override
    public void showBookView(final int isbn) {
        startActivity(DetailActivity.getStartIntent(this, isbn));
    }

    @OnClick(R.id.fab)
    public void onFabClick() {
        startActivity(new Intent(this, AddBookActivity.class));
    }
}