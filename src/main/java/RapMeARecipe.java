import mccormick.JavaHTMLParse;

public class RapMeARecipe {

    public static void main(String[] args) throws Exception {

        JavaHTMLParse http = new JavaHTMLParse();



        System.out.println("Testing 1 - Send Http GET request");
        String html = http.sendGet();

        html = html.substring(html.indexOf("itemid=\"")+9);
        html = html.substring(0,html.indexOf("}"));
        System.out.println(html);
    }


}
