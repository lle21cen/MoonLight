package org.techtown.ideaconcert.SettingsDir;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.techtown.ideaconcert.R;

public class TermsAndConditionActivity extends AppCompatActivity implements View.OnClickListener {

    // tac = terms and condition (이용약관)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_condition);

        Button backButton = findViewById(R.id.tac_back_btn);
        TextView backText = findViewById(R.id.tac_back_txt);
        backButton.setOnClickListener(this);
        backText.setOnClickListener(this);

        TextView tacView = findViewById(R.id.tac_text);
        tacView.setText(Html.fromHtml("<h3>문라이트 이용약관</h3>\n" +
                "<br>\n" +
                "\n" +
                "\n" +
                "<h3>제1조 목적</h3>\n" +
                "\n" +
                "\n" +
                "<p>이 약관은 ㈜문라이트(이하 회사 또는 문라이트)가 제공하는 제반 서비스의 이용과 관련하여 문라이트와 회원과의 권리, 의무 및 책임사항, 기타 필요한 사항을 규정함을 목적으로 합니다.</p>\n" +
                "<br>\n" +
                "\n" +
                "<h3>제2조 정의</h3>\n" +
                "\n" +
                "\n" +
                "<p>이 약관에서 사용하는 용어의 정의는 다음과 같습니다.</p> \n" +
                "\n" +
                "<p>① 서비스라 함은 구현되는 단말기(PC, TV, 휴대형단말기 등의 각종 유무선 장치를 포함)와 상관없이 회원이 이용할 수 있는 문라이트의\n" +
                "웹툰 관련 제반 서비스를 의미합니다.</p>\n" +
                "<p>② 회원이라 함은 문라이트의 서비스에 접속하여 이 약관에 따라 문라이트와 이용계약을 체결하고 문라이트가 제공하는 서비스를\n" +
                "이용하는 고객인 회원을 말합니다. </p>\n" +
                "<p>③ 비회원이라 함은 회원에 가입하지 않고 문라이트가 제공하는 무료 콘텐츠와 서비스를 이용하는 자를 말합니다. </p>\n" +
                "<p>④ 무료서비스라 함은 문라이트가 무료로 제공하는 각종 온라인 디지털콘텐츠(콘텐츠) 및 제반 서비스를 의미합니다. </p>\n" +
                "<p>⑤ 유료서비스라 함은 문라이트가 유료로 제공하는 각종 온라인 디지털콘텐츠(콘텐츠) 제반 서비스를 의미합니다. </p>\n" +
                "<p>⑥ 코인이라 함은 유료 콘텐츠 중 웹툰, 만화 서비스를 이용하기 위해 회원이 구입하는 사이버머니를 말합니다. </p>\n" +
                "<p>⑦ 콘텐츠라 함은 문라이트가 영리를 목적으로 제공하는 콘텐츠를 말합니다. </p>\n" +
                "<p>⑧ 정기결제상품이라 함은 회원이 등록한 결제수단을 통하여 월 단위로 이용요금이 자동으로 결제되고 이용기간이 자동 갱신되는 서비스를\n" +
                "말합니다. 정기결제 상품에는 무제한이용권과 코인 정기적립상품, 코믹스 이용권 등이 있습니다.</p>"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tac_back_btn:
            case R.id.tac_back_txt:
                finish();
                break;
        }
    }
}
