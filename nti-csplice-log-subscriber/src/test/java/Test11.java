import com.nti56.csplice.log.utils.LogSubUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 类说明: <br/>
 *
 * @author huangyubin <br/>
 * @version 1.0
 * @date 2022/4/6 17:51<br/>
 * @since JDK 1.8
 */
@Slf4j
public class Test11 {

    public static void main(String[] args) throws InterruptedException {
        LogSubUtils.subscribe("redis://139.159.194.101:21181"
        ,"csplice_log_1512109745675354113",(c,m)->{
                    System.out.println(c);
                    System.out.println(m);
                });
    }
}
