package app.dpapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class KanBanWebActivity extends AppCompatActivity {

    private WebView wv_kanban;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanban);
        wv_kanban=findViewById(R.id.wv_kanban);
        wv_kanban.getSettings().setJavaScriptEnabled(true);
        wv_kanban.setWebViewClient(new WebViewClient());
        wv_kanban.loadUrl(getString(R.string.url_output));
    }
}