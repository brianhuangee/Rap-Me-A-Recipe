package mccormick;

public class Pull {

    public static String getElementData(String elementTitle, String data) {
        String elementData = data.substring(data.indexOf("\"" + elementTitle + "\"") + elementTitle.length() + 4);
        elementData = elementData.substring(0, elementData.indexOf("\""));
        return elementData;
    }
}
