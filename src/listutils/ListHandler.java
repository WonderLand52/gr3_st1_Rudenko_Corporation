package listutils;


import java.util.List;

public class ListHandler {

    public static String listToString(List objects){

        if(objects.isEmpty()) {
            return "";
        }

        StringBuffer sb = new StringBuffer();

        for(Object obj: objects) {
            sb.append(obj);
        }

        return sb.toString();
    }
}
