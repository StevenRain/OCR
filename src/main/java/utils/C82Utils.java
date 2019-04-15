package utils;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import dto.UserDto;
import javafx.util.Pair;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.InputStream;
import java.util.Map;

public class C82Utils extends HttpUtils {

    private C82Utils() {
    }

    public static Pair<String, Image> getImageCodeAndCookie() {
        String url = "https://82cp.com/lottery-api/image/code?" + System.currentTimeMillis();
        HttpGet get = new HttpGet(url);
        try {
            HttpResponse response = getHttpClient().execute(get);
            String cookie = response.getFirstHeader("Set-Cookie").getValue();
            InputStream inputStream = response.getEntity().getContent();
            Image image = ImageIO.read(inputStream).getSubimage(0,0,100,40);
            return new Pair<>(cookie, image);
        }catch (Exception e) {
            return new Pair<>("", null);
        }
    }

    public static String register(UserDto userDto, String verifyCode, String cookie) {
        Map<String, String> map = Maps.newHashMap();
        map.put("account", userDto.getUsername());
        map.put("password", userDto.getPassword());
        map.put("confirmPassword", userDto.getPassword());
        map.put("imageCode", verifyCode);
        map.put("invitationId", userDto.getInvitationId());

        String payload = new Gson().toJson(map);
        String url = "https://82cp.com/lottery-api//login/register";

        Map<String, String> headerMap = Maps.newHashMap();
        headerMap.put("cookie", cookie);
        headerMap.put("content-type", "application/json;charset=UTF-8");
        return HttpUtils.sendPostByJsonData(url, headerMap, payload);
    }
}
