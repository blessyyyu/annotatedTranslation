package com.example.annotatedTranslation;

import com.swjtu.lang.LANG;
import com.swjtu.querier.Querier;
import com.swjtu.trans.AbstractTranslator;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author Yu Shaoqing
 * @date 2021/7/24/19:06
 */
public class GoogleTranslatorTest {
    @Test
    void testGoogle(){
        Querier<AbstractTranslator> querierTrans = new Querier<>();
        String q ="ご注意 表示よりも実際の付与数・付与率が少ない場合があります（付与上限、未確定の付与等）詳細を見る詳細を閉じる各特典には「1注文あたりの獲得上限」が設定されている場合があり、1注文あたりの獲得上限を超えた場合、表示されている獲得率での獲得はできません。各特典の1注文あたりの獲得上限は、各特典の詳細ページをご確認ください。以下の「獲得数が表示よりも少ない場合」に該当した場合も、表示されている獲得率での獲得はできません。各特典には「一定期間中の獲得上限（期間中獲得上限）」が設定されている場合があり、期間中獲得上限を超えた場合、表示されている獲得数での獲得はできません。各特典の期間中獲得上限は、各特典の詳細ページをご確認ください。「PayPaySTEP（PayPayモール特典）」は、獲得率の基準となる他のお取引についてキャンセル等をされたことで、獲得条件が未達成となる場合があります。この場合、表示された獲得数での獲得はできません。なお、詳細はPayPaySTEPの ヘルプページ でご確認ください。ヤフー株式会社またはPayPay株式会社が、不正行為のおそれがあると判断した場合（複数のYahoo! JAPAN IDによるお一人様によるご注文と判断した場合を含みますがこれに限られません）には、表示された獲得数の獲得ができない場合があります。その他各特典の詳細は内訳欄のページからご確認ください";
        querierTrans.setParams(LANG.AUTO, LANG.ZH,q);
        querierTrans.attach(new com.swjtu.trans.impl.GoogleTranslator());
        List<String> resultTrans = querierTrans.execute();
        for (String str : resultTrans) {
            System.out.println(str);
        }
    }
}
