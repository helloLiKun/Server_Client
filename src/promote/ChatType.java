package promote;

import java.lang.reflect.Field;

/**
 * Created by liKun on 2018/1/5 0005.
 */
class ChatType {
    private String CONNECT_TEST;
    private String CONNECT_SUCCESS;
    private String PRIVARE_CHAT;
    private String PUBLIC_CHAT;
    private String LOGIN_NAME;
    private String LOGIN_NAME_EXIT;
    private String LOGIN_SUCCESS;
    private String EXITE_SYSTEM;
    private String EXITE_SUCCESS;

    public ChatType() {
    }

    //通过此静态方法对属性赋值“__@+属性名+__”
    public static ChatType getChatType() {
        ChatType chatType = new ChatType();
        Class c = ChatType.class;
        Field[] fields = c.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field f : fields) {
                try {
                    f.setAccessible(true);
                    f.set(chatType, "__@" + f.getName() + "__");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return chatType;
    }

    public String getCONNECT_SUCCESS() {
        return CONNECT_SUCCESS;
    }

    public void setCONNECT_SUCCESS(String CONNECT_SUCCESS) {
        this.CONNECT_SUCCESS = CONNECT_SUCCESS;
    }

    public String getPRIVARE_CHAT() {
        return PRIVARE_CHAT;
    }

    public void setPRIVARE_CHAT(String PRIVARE_CHAT) {
        this.PRIVARE_CHAT = PRIVARE_CHAT;
    }

    public String getPUBLIC_CHAT() {
        return PUBLIC_CHAT;
    }

    public void setPUBLIC_CHAT(String PUBLIC_CHAT) {
        this.PUBLIC_CHAT = PUBLIC_CHAT;
    }

    public String getLOGIN_NAME() {
        return LOGIN_NAME;
    }

    public void setLOGIN_NAME(String LOGIN_NAME) {
        this.LOGIN_NAME = LOGIN_NAME;
    }

    public String getLOGIN_SUCCESS() {
        return LOGIN_SUCCESS;
    }

    public void setLOGIN_SUCCESS(String LOGIN_SUCCESS) {
        this.LOGIN_SUCCESS = LOGIN_SUCCESS;
    }

    public String getLOGIN_NAME_EXIT() {
        return LOGIN_NAME_EXIT;
    }

    public void setLOGIN_NAME_EXIT(String LOGIN_NAME_EXIT) {
        this.LOGIN_NAME_EXIT = LOGIN_NAME_EXIT;
    }
    public String getCONNECT_TEST() {
        return CONNECT_TEST;
    }

    public void setCONNECT_TEST(String CONNECT_TEST) {
        this.CONNECT_TEST = CONNECT_TEST;
    }

    public String getEXITE_SYSTEM() {
        return EXITE_SYSTEM;
    }

    public void setEXITE_SYSTEM(String EXITE_SYSTEM) {
        this.EXITE_SYSTEM = EXITE_SYSTEM;
    }

    public String getEXITE_SUCCESS() {
        return EXITE_SUCCESS;
    }

    public void setEXITE_SUCCESS(String EXITE_SUCCESS) {
        this.EXITE_SUCCESS = EXITE_SUCCESS;
    }
}
